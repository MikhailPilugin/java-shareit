package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class User {
    private long id;
    private String name;
    @Email
    @NotBlank
    @NotEmpty
    private String email;
}
