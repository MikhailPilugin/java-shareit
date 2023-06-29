package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */

@Data
@RequiredArgsConstructor
public class ItemDto {
    private long id;

    @NonNull
    @NotEmpty
    @NotBlank
    private final String name;

    @NonNull
    @NotEmpty
    @NotBlank
    private final String description;

    @NonNull
    private Boolean available;
    private long owner;
    private ItemRequest request;
}
