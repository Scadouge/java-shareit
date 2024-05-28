package ru.practicum.shareit.booking.repository;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByBookerIdOrderByEndDesc(Long bookerId);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByBookerIdAndStartAfterOrderByEndDesc(Long bookerId, LocalDateTime now);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByBookerIdAndEndBeforeOrderByEndDesc(Long bookerId, LocalDateTime now);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByBookerIdAndStatusOrderByEndDesc(Long bookerId, BookingStatus status);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime start, LocalDateTime end);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByEndDesc(Long ownerId, LocalDateTime now);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(Long ownerId, LocalDateTime now);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start, LocalDateTime end);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByItemOwnerIdAndStatusOrderByEndDesc(Long ownerId, BookingStatus status);

    @Fetch(FetchMode.JOIN)
    List<Booking> findAllByItemOwnerIdOrderByEndDesc(Long ownerId);

    <T> List<T> findAllByItemIdIn(Set<Long> itemIds, Class<T> type);

    List<Booking> findAllByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, LocalDateTime dateTime);
}
