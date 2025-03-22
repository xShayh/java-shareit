package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.Collection;

public interface ItemStorage {

    Item createItem(User user, ItemDto item);

    Item updateItem(User user, long id, ItemDto item);

    Item getItemById(long id);

    Collection<Item> getAllItems(long userId);

    Collection<Item> searchItems(String text);
}
