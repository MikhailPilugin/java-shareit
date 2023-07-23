package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User add(User user);

    User update(User user);

    User getById(long userId);

    List<User> getAll();

    void delete(long userId);

    void checkUser(long userId);
}
