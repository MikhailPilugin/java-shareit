package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailExistingException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User add(User user) {
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        checkUserAlreadyRegistered(user);
        user = updateFields(user);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User getById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id=%d not found", userId)));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(long userId) {
        checkUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("User with id=%d not found", userId));
        }
    }

    private void checkUserAlreadyRegistered(User user) {
        Optional<User> registeredUser = userRepository.findByEmailContainingIgnoreCase(user.getEmail());
        if (registeredUser.isPresent() && !registeredUser.get().getId().equals(user.getId())) {
            throw new EmailExistingException(
                    String.format("User with email: %s is already registered", user.getEmail()));
        }
    }

    private User updateFields(User user) {
        User savedUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id=%d not found", user.getId())));
        String name = user.getName() == null ? savedUser.getName() : user.getName();
        String email = user.getEmail() == null ? savedUser.getEmail() : user.getEmail();
        return User.builder()
                .id(user.getId())
                .name(name)
                .email(email)
                .build();
    }
}
