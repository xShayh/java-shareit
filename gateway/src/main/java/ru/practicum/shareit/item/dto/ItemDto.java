package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private long id;

    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может состоять из символов пробела")
    private String name;

    @NotNull(message = "Описание не может быть пустым")
    @NotBlank(message = "Описание не может состоять из символов пробела")
    private String description;

    @NotNull(message = "Статус доступности не может быть пустым")
    private Boolean available;

    private Long requestId;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Collection<CommentDto> comments;
}
