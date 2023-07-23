package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody @Valid ItemDto itemDto) {
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable("id") long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemOwnerDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                @PathVariable("id") long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemOwnerDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                     @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String query,
                                @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        return itemService.search(query, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long itemId,
                                 @RequestBody @Valid CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
