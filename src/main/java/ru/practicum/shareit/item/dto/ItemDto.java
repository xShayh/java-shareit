package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
public class ItemDto {
    private long id;

    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может состоять из символов пробела")
    private String name;

    @NotNull(message = "Описание не может быть пустым")
    @NotBlank(message = "Описание не может состоять из символов пробела")
    private String description;

    @NotNull(message = "Статус доступности не может быть пустым")
    @NotBlank(message = "Статус доступности не может состоять из символов пробела")
    private String available;

    private User owner;
    private ItemRequest request;
}
