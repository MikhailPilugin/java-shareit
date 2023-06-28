package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping
    public User getUser() {
        return new User();
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping
    public void delUser(@RequestBody User user) {

    }


}
