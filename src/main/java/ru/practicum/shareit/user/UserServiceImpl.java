package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository;

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll().values();
    }

    @Override
    public User getUser(Integer userId) {
        return userRepository.get(userId);
    }

    @Override
    public User saveUser(User user) {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("There is no email");
        }

        return userRepository.add(user);
    }

    @Override
    public User updateUser(User user, Integer userId) {
        return userRepository.update(user, userId);
    }
}
