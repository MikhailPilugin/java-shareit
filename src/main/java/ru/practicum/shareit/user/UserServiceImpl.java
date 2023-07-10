package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional getUser(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Object saveUser(User user) {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("There is no email");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Integer userId) {
        return userRepository.update(user, userId);
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.delete(userId);
    }
}
