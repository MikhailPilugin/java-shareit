package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */

//id — уникальный идентификатор запроса;
//description — текст запроса, содержащий описание требуемой вещи;
//requestor — пользователь, создавший запрос;
//created — дата и время создания запроса.

@Data
public class ItemRequest {
    private long id;
    private String description;
    private User requestor;
    private LocalDate created;
}
