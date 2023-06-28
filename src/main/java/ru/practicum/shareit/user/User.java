package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */

// id — уникальный идентификатор пользователя;
//name — имя или логин пользователя;
//email — адрес электронной почты (учтите, что два пользователя не могут
//иметь одинаковый адрес электронной почты).

@Data
public class User {
    private long id;
    private String name;
    @Email
    @NotBlank
    @NotEmpty
    private String email;
}
