package ru.practicum.shareit.user;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Setter
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          @Qualifier("userMapper") UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@RequestBody @Valid UserDto userDto) {
        return userMapper.toUserDto(userService.add(userMapper.toUser(userDto)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") long userId,
                          @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto)
                .withId(userId);
        user = userService.update(user);
        return userMapper.toUserDto(user);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") long userId) {
        return userMapper.toUserDto(userService.getById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long userId) {
        userService.delete(userId);
    }
}
