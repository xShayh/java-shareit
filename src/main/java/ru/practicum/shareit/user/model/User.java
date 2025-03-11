package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @NotNull(message = "Поле name не может быть пустым")
    @NotBlank(message = "Поле name не может состоять из символов пробела")
    @Column(name = "user_name")
    private String name;

    @NotNull(message = "Поле email не может быть пустым")
    @NotBlank(message = "Поле email не может состоять из символов пробела")
    @Email(message = "Некорректный формат email")
    @Column(name = "email")
    private String email;
}
