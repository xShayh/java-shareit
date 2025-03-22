package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ConditionException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public ItemDto createItem(long userId, ItemSaveDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            ItemRequest request = requestRepository.findById(requestId).orElseThrow(
                    () -> new ObjectNotFoundException("Запрос с id = " + requestId + " не найден"));
            item.setRequest(request);
        }
        log.info("Item has been created {}, userId={}", item, userId);
        return ItemMapper.toItemDto(itemRepository.save(item));

    }

    @Override
    public ItemDto updateItem(long userId, long id, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Вещь с id = " + id + " не найдена"));
        item.setId(id);
        if (user.getId() != item.getOwner().getId()) {
            throw new ObjectNotFoundException("Пользователь может обновлять только свои вещи");
        }
        item.setOwner(user);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Вещь с id = " + id + " не найдена"));

        Collection<Booking> nextBookings = bookingRepository.findAllByItemIdAndStartAfterOrderByStartAsc(id,
                LocalDateTime.now());
        LocalDateTime nextBooking = nextBookings.stream()
                .map(Booking::getStart)
                .findFirst()
                .orElse(null);
        LocalDateTime lastBooking = nextBookings.stream()
                .map(Booking::getStart)
                .reduce((first, last) -> last)
                .orElse(null);

        Collection<Comment> comments = commentRepository.findAllByItemId(id);
        Collection<CommentDto> commentsDto = comments.stream().map(CommentMapper::toCommentDto).toList();

        ItemDto itemDto = ItemMapper.toItemDto(item, lastBooking, nextBooking, commentsDto);
        return itemDto;
    }

    @Override
    public Collection<ItemDto> getAllItems(long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));

        Collection<ItemDto> itemsDto = itemRepository.findAllByOwnerId(userId).stream().map(ItemMapper::toItemDto).toList();
        Collection<Long> itemsId = itemsDto.stream().map(ItemDto::getId).toList();

        Collection<Booking> futureBookings = bookingRepository
                .findAllByItemIdInAndStartAfterOrderByStartDesc(itemsId, LocalDateTime.now());

        Collection<Booking> pastBookings = bookingRepository
                .findAllByItemIdInAndEndBeforeOrderByStartDesc(itemsId, LocalDateTime.now());

        Collection<Comment> comments = commentRepository.findAllByItemIdIn(itemsId);
        Collection<CommentDto> commentsDto = comments.stream().map(CommentMapper::toCommentDto).toList();

        for (ItemDto itemDto : itemsDto) {
            for (Booking futureBooking : futureBookings) {
                if (futureBooking.getItem().getId() == itemDto.getId()) {
                    if (itemDto.getNextBooking().isAfter(futureBooking.getStart())) {
                        itemDto.setNextBooking(futureBooking.getStart());
                    }
                }
            }

            for (Booking pastBooking : pastBookings) {
                if (pastBooking.getItem().getId() == itemDto.getId()) {
                    if (itemDto.getLastBooking().isBefore(pastBooking.getStart())) {
                        itemDto.setLastBooking(pastBooking.getStart());
                    }
                }
            }

            Collection<CommentDto> commentsByItem = itemDto.getComments();
            for (CommentDto commentDto : commentsDto) {
                if (commentDto.getItem().getId() == itemDto.getId()) {
                    if (commentsByItem == null) {
                        commentsByItem = new ArrayList<>();
                    }
                    commentsByItem.add(commentDto);
                }
            }
            itemDto.setComments(commentsByItem);
        }
        return itemsDto;
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepository.searchItems(text).stream().map(ItemMapper::toItemDto).toList();
        }
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto comment) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ObjectNotFoundException("Вещь с id = " + itemId + " не найдена"));
        Booking booking = bookingRepository
                .findByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(userId, itemId, LocalDateTime.now());
        if (booking == null) {
            throw new ConditionException("Бронирование вещи не подтверждено");
        }
        comment.setItem(item);
        comment.setAuthorName(user.getName());
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(comment, user)));
    }

    @Override
    public Collection<ItemResponseDto> getItemsByRequestId(long requestId) {
        Collection<ItemResponseDto> items = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemResponseDto)
                .toList();
        return items;
    }
}