package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ConditionException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto createBooking(long userId, BookingInputDto bookingInputDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));
        Item item = itemRepository.findById(bookingInputDto.getItemId()).orElseThrow(
                () -> new ObjectNotFoundException("Вещь с id = " + bookingInputDto.getItemId() + " не найдена"));
        if (!item.getAvailable()) {
            throw new ConditionException("Вещь с id = " + item.getId() + "не доступна для бронирования");
        }
        Booking booking = bookingMapper.toBooking(bookingInputDto, item, user);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approveBooking(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ObjectNotFoundException("Бронирование с id = " + bookingId + "не найдено"));
        if (userId != booking.getItem().getOwner().getId()) {
            throw new ConditionException("Только владелец вещи может управлять бронированием");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ObjectNotFoundException("Бронирование с id = " + bookingId + "не найдено"));
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(
                () -> new ObjectNotFoundException("Вещь с id = " + booking.getItem().getId() + " не найдена"));
        if ((userId != booking.getBooker().getId()) && (userId != item.getOwner().getId())) {
            throw new ConditionException("Просмотр бронирования доступен только владельцу вещи или человеку," +
                    " который забронировал вещь");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getAllBookingsByUser(long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));
        Collection<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllBookingsCurrent(userId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId,
                        LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.REJECTED);
                break;
            default:
                throw new ConditionException("Неверное значение параметра state" + state.toString());
        }
        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByUserItems(long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));
        Collection<Item> items = itemRepository.findAllByOwnerId(userId);
        Collection<Long> ids = items.stream().map(Item::getId).toList();
        if (items.size() == 0) {
            throw new ObjectNotFoundException("У пользователя нет вещей");
        }
        Collection<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemIdInOrderByStartDesc(ids);
                ;
                break;
            case CURRENT:
                bookings = bookingRepository.findAllBookingsCurrent(ids, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemIdInAndEndBeforeOrderByStartDesc(ids,
                        LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemIdInAndStartAfterOrderByStartDesc(ids,
                        LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(ids,
                        BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(ids,
                        BookingStatus.REJECTED);
                break;
            default:
                throw new ConditionException("Неверное значение параметра state" + state.toString());
        }
        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }
}