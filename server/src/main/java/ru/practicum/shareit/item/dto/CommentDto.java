package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;
    private String text;
    private Item item;
    private String authorName;
    private LocalDateTime created;
}