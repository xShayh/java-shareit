package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUserById(long id);

    User createUser(User user);

    User updateUser(long id, User user);

    void deleteUser(long id);
}
