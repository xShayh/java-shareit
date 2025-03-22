package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(long userId, ItemDto item);

    ItemDto updateItem(long userId, long id, ItemDto item);

    ItemDto getItemById(long id);

    Collection<ItemDto> getAllItems(long userId);

    Collection<ItemDto> searchItems(String text);

    CommentDto addComment(long userId, long itemId, CommentDto comment);
}
