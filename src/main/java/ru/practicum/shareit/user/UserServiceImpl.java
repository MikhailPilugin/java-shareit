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
    public User getUser(Long userId) {
        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isPresent()) {
            User newUser = optUser.get();
            return newUser;
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public User saveUser(User user) {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("There is no email");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        User newUser = optUser.get();

        if (user.getId() >= 1) {
            newUser.setId(user.getId());
        }

        if (user.getName() != null) {
            newUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }

        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();

        userRepository.delete(user);
    }
}
