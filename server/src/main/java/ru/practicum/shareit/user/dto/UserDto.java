package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class UserDto {
    private Long id;
//    @NotBlank
    private String name;
//    @NotBlank
//    @Email
    private String email;
}
