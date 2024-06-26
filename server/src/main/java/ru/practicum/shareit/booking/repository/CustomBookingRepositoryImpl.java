package ru.practicum.shareit.booking.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class CustomBookingRepositoryImpl implements CustomBookingRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Booking> findLastBookingWithStatus(Set<Long> itemIds, LocalDateTime time, BookingStatus status) {
        String sql = "SELECT DISTINCT ON (item_id) bk.* FROM bookings bk " +
                "WHERE bk.item_id IN :itemIds AND (bk.date_end < :time OR bk.date_start < :time AND bk.date_end > :time) AND bk.status = :status " +
                "ORDER BY bk.item_id, bk.date_end DESC";
        return entityManager.createNativeQuery(sql, Booking.class)
                .setParameter("itemIds", itemIds)
                .setParameter("time", time)
                .setParameter("time", time)
                .setParameter("time", time)
                .setParameter("status", status.toString())
                .getResultList();
    }

    @Override
    public List<Booking> findNextBookingWithStatus(Set<Long> itemIds, LocalDateTime time, BookingStatus status) {
        String sql = "SELECT DISTINCT ON (item_id) bk.* FROM bookings bk " +
                "WHERE bk.item_id IN :itemIds AND bk.date_start > :time AND bk.status = :status " +
                "ORDER BY bk.item_id, bk.date_start";
        return entityManager.createNativeQuery(sql, Booking.class)
                .setParameter("itemIds", itemIds)
                .setParameter("time", time)
                .setParameter("status", status.toString())
                .getResultList();
    }

//    @Override
//    public List<Booking> findLastBookingWithStatus(Set<Long> itemIds, LocalDateTime time, BookingStatus status) {
//        String sql = "SELECT bk.* FROM bookings bk " +
//                "INNER JOIN (SELECT bk.item_id, MAX(date_end) as max_date FROM bookings bk " +
//                "WHERE bk.item_id IN :itemIds AND (bk.date_end < :time OR bk.date_start < :time AND bk.date_end > :time) AND bk.status = :status " +
//                "GROUP BY bk.item_id) sb ON bk.item_id = sb.item_id AND bk.date_end = sb.max_date";
//        return entityManager.createNativeQuery(sql, Booking.class)
//                .setParameter("itemIds", itemIds)
//                .setParameter("time", time)
//                .setParameter("time", time)
//                .setParameter("time", time)
//                .setParameter("status", status.toString())
//                .getResultList();
//    }
//
//    @Override
//    public List<Booking> findNextBookingWithStatus(Set<Long> itemIds, LocalDateTime time, BookingStatus status) {
//        String sql = "SELECT bk.* FROM bookings bk " +
//                "INNER JOIN (SELECT bk.item_id, MIN(date_start) as min_date FROM bookings bk " +
//                "WHERE bk.item_id IN :itemIds AND bk.date_start > :time AND bk.status = :status " +
//                "GROUP BY bk.item_id) sb ON bk.item_id = sb.item_id AND bk.date_start = sb.min_date";
//        return entityManager.createNativeQuery(sql, Booking.class)
//                .setParameter("itemIds", itemIds)
//                .setParameter("time", time)
//                .setParameter("status", status.toString())
//                .getResultList();
//    }
}