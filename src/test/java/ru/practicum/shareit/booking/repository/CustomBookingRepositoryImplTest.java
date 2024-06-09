package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomBookingRepositoryImplTest {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    void test1() {
        User booker = new User(null, "Booker 1", "booker1@mail.com");
        User owner = new User(null, "Owner 1", "owner1@mail.com");
        userRepository.save(booker);
        userRepository.save(owner);

        Item item1 = new Item(null, "Item 1", "Item 1 desc", true, owner.getId(), null);
        Item item2 = new Item(null, "Item 2", "Item 2 desc", true, owner.getId(), null);
        itemRepository.save(item1);
        itemRepository.save(item2);

        LocalDateTime now = LocalDateTime.of(2010, 1, 1, 10, 0);
        Booking booking1 = new Booking(null, item1, BookingStatus.APPROVED, booker, now.minusYears(2), now.minusYears(2));
        Booking booking2 = new Booking(null, item2, BookingStatus.APPROVED, booker, now.minusYears(4), now.minusYears(4));
        Booking booking3 = new Booking(null, item1, BookingStatus.APPROVED, booker, now.minusYears(1), now.minusYears(1));
        Booking booking4 = new Booking(null, item1, BookingStatus.APPROVED, booker, now.plusYears(3), now.plusYears(3));
        Booking booking5 = new Booking(null, item2, BookingStatus.APPROVED, booker, now.plusYears(2), now.plusYears(2));
        Booking booking6 = new Booking(null, item1, BookingStatus.APPROVED, booker, now.plusYears(5), now.plusYears(5));
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        bookingRepository.save(booking5);
        bookingRepository.save(booking6);

        Map<Long, List<Booking>> lastBookings = bookingRepository.findLastBookingWithStatus(Set.of(item1.getId(), item2.getId()),
                now, BookingStatus.APPROVED, Booking.class).stream().collect(Collectors.groupingBy(b -> b.getItem().getId()));
        Map<Long, List<Booking>> nextBookings = bookingRepository.findNextBookingWithStatus(Set.of(item1.getId(), item2.getId()),
                now, BookingStatus.APPROVED, Booking.class).stream().collect(Collectors.groupingBy(b -> b.getItem().getId()));
        assertEquals(2, lastBookings.entrySet().size());
        assertEquals(2, nextBookings.entrySet().size());
        assertEquals(booking3.getId(), lastBookings.get(item1.getId()).stream().findFirst().orElseThrow().getId());
        assertEquals(booking2.getId(), lastBookings.get(item2.getId()).stream().findFirst().orElseThrow().getId());
        assertEquals(booking4.getId(), nextBookings.get(item1.getId()).stream().findFirst().orElseThrow().getId());
        assertEquals(booking5.getId(), nextBookings.get(item2.getId()).stream().findFirst().orElseThrow().getId());
    }
}