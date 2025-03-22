package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper requestMapper;

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDto itemRequest) {
        User user = getUserById(userId);
        itemRequest.setRequester(user);
        return requestMapper.toItemRequestDto(requestRepository.save(requestMapper.toItemRequest(itemRequest)));
    }

    @Override
    public Collection<ItemRequestDto> getAllRequestsByUser(long userId) {
        User user = getUserById(userId);
        Collection<ItemRequestDto> requests = requestRepository
                .findAllByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(requestMapper::toItemRequestDtoWithResponse)
                .toList();
        return requests;
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(long userId) {
        User user = getUserById(userId);
        Collection<ItemRequestDto> requests = requestRepository
                .findAllByRequesterIdNotOrderByCreatedDesc(userId).stream()
                .map(requestMapper::toItemRequestDtoWithResponse)
                .toList();
        return requests;
    }

    @Override
    public ItemRequestDto getRequestById(long requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId).orElseThrow(
                () -> new ObjectNotFoundException("Запрос с id " + requestId + " не найден"));
        return requestMapper.toItemRequestDtoWithResponse(itemRequest);
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id " + userId + " не найден"));
    }
}