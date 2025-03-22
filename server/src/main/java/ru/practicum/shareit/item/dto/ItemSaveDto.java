package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSaveDto {
    private long id;

    @NotNull(message = "Поле name не может быть пустым")
    @NotBlank(message = "Поле name не может состоять из символов пробела")
    private String name;

    @NotNull(message = "Поле description не может быть пустым")
    @NotBlank(message = "Поле description не может состоять из символов пробела")
    private String description;

    @NotNull(message = "Поле available не может быть пустым")
    private Boolean available;

    private Long requestId;
}