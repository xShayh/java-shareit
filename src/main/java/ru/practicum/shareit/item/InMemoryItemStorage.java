package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConditionException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private long nextId = 0;
    private final Map<Long, Item> items = new HashMap<>();
    private final ItemMapper itemMapper;

    @Override
    public Item createItem(User user, ItemDto item) {
        if (item.getName() == null || item.getName().isEmpty() || item.getDescription() == null) {
            throw new ConditionException("Имя и/или описание не могут быть пустыми");
        }
        item.setId(getNextId());
        item.setOwner(user);
        if (item.getRequest() == null) {
            item.setRequest(new ItemRequest());
        }
        if (item.getAvailable() == null) {
            throw new ConditionException("Статус доступности вещи не может быть пустым");
        }
        Item itemToStorage = itemMapper.toItem(item);
        items.put(item.getId(), itemToStorage);
        return itemToStorage;
    }

    @Override
    public Item updateItem(User user, long id, ItemDto item) {
        if (user.getId() != items.get(id).getOwner().getId()) {
            throw new ObjectNotFoundException("Пользователь может обновлять только свои вещи");
        }
        if (!items.containsKey(id)) {
            throw new ObjectNotFoundException("Вещь с id " + id + " не найдена");
        }
        Item itemFromStorage = items.get(id);
        itemFromStorage.setId(id);
        if (item.getName() != null) {
            itemFromStorage.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemFromStorage.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            if (item.getAvailable().equals("true")) {
                itemFromStorage.setAvailable(true);
            }
            if (item.getAvailable().equals("false")) {
                itemFromStorage.setAvailable(false);
            }
        }
        items.put(itemFromStorage.getId(), itemFromStorage);
        return itemFromStorage;
    }

    @Override
    public Item getItemById(long id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new ObjectNotFoundException("Вещь с id " + id + " не найдена");
        }
    }

    @Override
    public Collection<Item> getAllItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .toList();
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        } else {
            return items.values().stream()
                    .filter(Item::isAvailable)
                    .filter(item -> item.getName().equalsIgnoreCase(text) ||
                            item.getDescription().equalsIgnoreCase(text))
                    .toList();
        }
    }

    private long getNextId() {
        return nextId++;
    }
}
