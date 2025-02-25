package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto getUserById(long id);

    UserDto createUser(UserDto user);

    UserDto updateUser(long id, UserDto user);

    void deleteUser(long id);
}
