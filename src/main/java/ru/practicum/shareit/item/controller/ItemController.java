package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.ValidationGroup;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    @Validated({ValidationGroup.OnCreate.class})
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Создание предмета itemDto={}", itemDto);
        return itemService.create(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        log.info("Получение предмета itemId={}", itemId);
        return itemService.get(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllUserItems(@RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Получение всех предметов пользователя userId={}", userId);
        return itemService.getAll(userId);
    }

    @PatchMapping("/{itemId}")
    @Validated({ValidationGroup.OnUpdate.class})
    public ItemDto updateItem(@PathVariable Long itemId,
                              @Valid @RequestBody ItemDto itemDto,
                              @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Обновление предмета пользователя userId={}, itemId={}, itemDto={}", userId, itemId, itemDto);
        return itemService.update(itemDto.toBuilder().id(itemId).build(), userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchAvailableItems(@RequestParam String text) {
        log.info("Поиск доступных предметов text={}", text);
        if (text.isBlank()) {
            return List.of();
        }
        return itemService.searchAvailableItems(text);
    }
}
