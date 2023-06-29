package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User getUser(Integer userId);

    User saveUser(User user);

    User updateUser(User user, Integer userId);

    void deleteUser(Integer userId);
}
