package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

//id — уникальный идентификатор вещи;
//name — краткое название;
//description — развёрнутое описание;
//available — статус о том, доступна или нет вещь для аренды;
//owner — владелец вещи;
//request — если вещь была создана по запросу другого пользователя, то в этом
//поле будет храниться ссылка на соответствующий запрос.

@Data
public class Item {
    private long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private boolean available;
    private long owner;
    private ItemRequest request;
}
