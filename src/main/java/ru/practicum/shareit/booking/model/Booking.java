package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.shareit.utils.SqlHelper.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = TABLE_BOOKINGS, schema = SCHEMA)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = BOOKING_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BOOKING_ITEM_ID)
    private Item item;

    @Column(name = BOOKING_ITEM_ID, insertable = false, updatable = false)
    private Long itemId;

    @Enumerated(EnumType.STRING)
    @Column(name = BOOKING_STATUS)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BOOKING_BOOKER_ID)
    private User booker;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = BOOKING_DATE_START)
    private LocalDateTime start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = BOOKING_DATE_END)
    private LocalDateTime end;
}
