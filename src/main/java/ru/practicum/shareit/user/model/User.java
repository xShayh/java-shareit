package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private long id;

    @NotNull(message = "Поле name не может быть пустым")
    @NotBlank(message = "Поле name не может состоять из символов пробела")
    private String name;

    @NotNull(message = "Поле email не может быть пустым")
    @NotBlank(message = "Поле email не может состоять из символов пробела")
    @Email(message = "Некорректный формат email")
    private String email;
}
