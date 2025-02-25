package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConditionException;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private int nextId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ObjectNotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new DuplicateDataException("Пользователь с email " + user.getEmail() + " уже существует. " +
                    "Добавление невозможно");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            throw new ConditionException("Некорректный формат email");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(long id, User user) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь с id " + id + " не найден");
        }
        User userFromStorage = users.get(id);
        userFromStorage.setId(id);
        if (user.getEmail() != null) {
            if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                throw new DuplicateDataException("Пользователь с email: " + user.getEmail() + " уже существует");
            }
            userFromStorage.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userFromStorage.setName(user.getName());
        }
        users.put(userFromStorage.getId(), userFromStorage);
        return userFromStorage;
    }

    @Override
    public void deleteUser(long id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь с id " + id + " не найден");
        }
        users.remove(id);
    }

    private int getNextId() {
        return nextId++;
    }
}
