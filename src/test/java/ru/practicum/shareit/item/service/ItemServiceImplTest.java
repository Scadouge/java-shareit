package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAllowedException;
import ru.practicum.shareit.item.args.CreateCommentArgs;
import ru.practicum.shareit.item.args.CreateItemArgs;
import ru.practicum.shareit.item.args.UpdateItemArgs;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;

    @Test
    void shouldCreateAndGetAndUpdateItem() {
        User otherUser = userRepository.save(new User(null, "User 1 name", "user1@mail.com"));
        User owner = userRepository.save(new User(null, "User 2 name", "user2@mail.com"));
        Item item = itemService.create(new CreateItemArgs(owner.getId(), "Item name", "Item desc", true, null));
        Item savedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertEquals(savedItem, item);
        assertEquals(savedItem, itemService.get(item.getId(), owner.getId()));
        assertThat(itemService.getAll(owner.getId(), 0, 10), contains(item));

        itemService.update(new UpdateItemArgs(null, "Updated desc", null), item.getId(), owner.getId());
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertEquals("Item name", updatedItem.getName());
        assertEquals("Updated desc", updatedItem.getDescription());
        assertThrows(NotAllowedException.class, () -> itemService.update(new UpdateItemArgs(null, "Updated desc", null), item.getId(), otherUser.getId()));
    }

    @Test
    void shouldCreateCommentByCorrectUsers() {
        User booker = userRepository.save(new User(null, "User 1 name", "user1@mail.com"));
        User owner = userRepository.save(new User(null, "User 2 name", "user2@mail.com"));
        Item item = itemService.create(new CreateItemArgs(owner.getId(), "Item name", "Item desc", true, null));
        bookingRepository.save(new Booking(null, item, BookingStatus.APPROVED, booker, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)));

        assertThrows(ValidationException.class, () -> itemService.createComment(new CreateCommentArgs(owner.getId(), item.getId(), "text")));
        Comment comment = itemService.createComment(new CreateCommentArgs(booker.getId(), item.getId(), "text"));
        Comment savedComment = commentRepository.findById(comment.getId()).orElseThrow();
        assertEquals(savedComment, comment);
        assertThrows(ValidationException.class, () -> itemService.createComment(new CreateCommentArgs(booker.getId(), item.getId(), "text")));
    }
}