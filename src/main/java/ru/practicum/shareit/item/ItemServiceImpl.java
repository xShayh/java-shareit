package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(long userId, ItemDto item) {
        User user = userStorage.getUserById(userId);
        return itemMapper.toItemDto(itemStorage.createItem(user, item));
    }

    @Override
    public ItemDto updateItem(long userId, long id, ItemDto item) {
        User user = userStorage.getUserById(userId);
        return itemMapper.toItemDto(itemStorage.updateItem(user, id, item));
    }

    @Override
    public ItemDto getItemById(long id) {
        return itemMapper.toItemDto(itemStorage.getItemById(id));
    }

    @Override
    public Collection<ItemDto> getAllItems(long userId) {
        return itemStorage.getAllItems(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        return itemStorage.searchItems(text).stream().map(ItemMapper::toItemDto).toList();
    }
}
