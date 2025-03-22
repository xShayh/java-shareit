package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;

import java.util.Collection;

public interface ItemService {

    ItemDto createItem(long userId, ItemSaveDto item);

    ItemDto updateItem(long userId, long id, ItemDto item);

    ItemDto getItemById(long id);

    Collection<ItemDto> getAllItems(long userId);

    Collection<ItemDto> searchItems(String text);

    CommentDto addComment(long userId, long itemId, CommentDto comment);

    Collection<ItemResponseDto> getItemsByRequestId(long requestId);
}