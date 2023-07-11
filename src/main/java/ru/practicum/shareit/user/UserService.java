package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Collection<User> getAllUsers();

    User getUser(Long userId);

    User saveUser(User user);

    User updateUser(User user, Long userId);

    void deleteUser(Long userId);
}
