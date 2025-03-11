package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public UserDto getUserById(long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + id + " не найден")));
    }

    @Override
    public UserDto createUser(UserDto user) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(user)));
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id = " + id + " не найден"));
        userDto.setId(user.getId());
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if ((userDto.getEmail() != null) && (!user.getEmail().equals(userDto.getEmail()))) {
            if (userRepository.findByEmail(userDto.getEmail())
                    .stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                throw new DuplicateDataException("Пользователь с email: " + user.getEmail() + " уже существует. " +
                        "Обновление невозможно");
            }
            user.setEmail(userDto.getEmail());
        }
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}