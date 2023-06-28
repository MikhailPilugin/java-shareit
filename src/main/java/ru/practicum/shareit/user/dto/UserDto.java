package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@RequiredArgsConstructor
public class UserDto {
    private long id;
    private final String name;
    @Email
    @NotBlank
    @NotEmpty
    private final String email;
}
