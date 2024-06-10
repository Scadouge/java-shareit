package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserRepository userRepository;
    private final UserService userService;

    @Test
    void shouldSaveAndDeleteUser() {
        User user = userService.create(new User(null, "User 1", "user1@mail.com"));
        User savedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(user, savedUser);
        userRepository.deleteById(user.getId());
        assertThrows(NotFoundException.class, () -> userService.get(user.getId()));
    }
}