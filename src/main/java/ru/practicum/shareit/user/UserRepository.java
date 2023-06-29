package ru.practicum.shareit.user;

import java.util.Map;

public interface UserRepository {
    Map<Integer, User> findAll();

    User get(Integer userId);

    User add(User user);

    User update(User user, Integer userId);

    void delete(Integer userId);
}
