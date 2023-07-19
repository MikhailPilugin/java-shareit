package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailExistingException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto add(User user) {
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        User user = userMapper.toUser(userDto).withId(userId);
        checkUserAlreadyRegistered(user);
        user = updateFields(user);
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public User getById(long userId) {
        Optional<User> optionalUserDto = userRepository.findById(userId);

        if (optionalUserDto.isPresent()) {
            return optionalUserDto.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
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
            throw new UserNotFoundException("User not found");
        }
    }

    private void checkUserAlreadyRegistered(User user) {
        Optional<User> registeredUser = userRepository.findByEmailContainingIgnoreCase(user.getEmail());
        if (registeredUser.isPresent() && !registeredUser.get().getId().equals(user.getId())) {
            throw new EmailExistingException("Email is exist");
        }
    }

    private User updateFields(User user) {
        User savedUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new UserNotFoundException("User not found"));
        String name = user.getName() == null ? savedUser.getName() : user.getName();
        String email = user.getEmail() == null ? savedUser.getEmail() : user.getEmail();
        return User.builder()
                .id(user.getId())
                .name(name)
                .email(email)
                .build();
    }
}

