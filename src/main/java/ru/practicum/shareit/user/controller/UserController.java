package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.validation.RequiredUserData;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequiredUserData @RequestBody UserDto userDto) {
        log.info("Создание пользователя userDto={}", userDto);
        return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Получение пользователя userId={}", userId);
        return userService.get(userId);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userService.getAll();
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        log.info("Обновление пользователя userDto={}", userDto);
        return userService.update(userDto.toBuilder().id(userId).build());
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя userId={}", userId);
        userService.delete(userId);
    }
}


