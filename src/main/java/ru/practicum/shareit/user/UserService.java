package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto add(User user);

    UserDto update(UserDto user, Long userId);

    User getById(long userId);

    List<User> getAll();

    void delete(long userId);

    void checkUser(long userId);
}

