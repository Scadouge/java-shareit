package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UniqueFieldConflictException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.*;

@Repository
@Slf4j
public class UserStorageInMemory implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    private long generateId() {
        return id++;
    }

    private void checkEmail(User user) {
        Optional<User> emailTaken = users.values().stream()
                .filter(u -> !Objects.equals(u.getId(), user.getId()))
                .filter(u -> u.getEmail().equals(user.getEmail())).findFirst();
        if (emailTaken.isPresent()) {
            throw new UniqueFieldConflictException(String.format("Email уже используется email=%s", user.getEmail()));
        }
    }

    @Override
    public User create(User user) {
        checkEmail(user);
        long id = generateId();
        User updatedUser = user.toBuilder().id(id).build();
        users.put(id, updatedUser);
        log.info("Пользователь создан user={}", updatedUser);
        return updatedUser;
    }

    @Override
    public User get(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException(id);
        }
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User update(User user) {
        User oldUser = users.get(user.getId());
        if (oldUser == null) {
            throw new NotFoundException(user.getId());
        }
        if (user.getEmail() != null) {
            checkEmail(user);
        }
        User updatedUser = UserMapper.updateModel(oldUser, user);
        users.put(user.getId(), updatedUser);
        return updatedUser;
    }

    @Override
    public boolean delete(Long id) {
        User oldValue = users.remove(id);
        return oldValue != null;
    }
}
