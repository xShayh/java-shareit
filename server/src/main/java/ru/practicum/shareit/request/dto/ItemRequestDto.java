package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDto {
    private long id;

    @NotNull(message = "Поле description не может быть пустым")
    @NotBlank(message = "Поле description не может состоять из символов пробела")
    private String description;

    private User requester;

    private LocalDateTime created;

    private Collection<ItemResponseDto> items;
}