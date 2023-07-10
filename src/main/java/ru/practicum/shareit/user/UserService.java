package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Collection<User> getAllUsers();

    Optional getUser(Integer userId);

    Object saveUser(User user);

    User updateUser(User user, Integer userId);

    void deleteUser(Integer userId);
}
