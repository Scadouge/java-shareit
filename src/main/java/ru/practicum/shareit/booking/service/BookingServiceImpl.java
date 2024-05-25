package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto createBooking(Long userId, BookingCreateDto bookingCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException(bookingCreateDto.getItemId()));
        if (!item.getAvailable()) {
            throw new ValidationException("Предмет недоступен для бронирования");
        }
        if (Objects.equals(item.getOwnerId(), userId)) {
            throw new NotFoundException(item.getId(), "Невозможно забронировать свой предмет");
        }
        Booking booking = bookingMapper.toModel(bookingCreateDto, user, item);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    @Override
    public BookingDto updateBookingStatus(Long userId, Long bookingId, boolean approved) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(bookingId));
        if (!Objects.equals(userId, booking.getItem().getOwnerId())) {
            throw new NotFoundException(bookingId);
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Статус бронирования изменить уже нельзя");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(bookingId));
        if (!Objects.equals(userId, booking.getBooker().getId()) &&
                !Objects.equals(userId, booking.getItem().getOwnerId())) {
            throw new NotFoundException(bookingId);
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingsForUser(Long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        List<Booking> result;
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByBookerIdOrderByEndDesc(userId);
                break;
            case CURRENT:
                result = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                result = bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                result = bookingRepository.findAllByBookerIdAndStartAfterOrderByEndDesc(userId, LocalDateTime.now());
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, BookingStatus.REJECTED);
                break;
            default:
                log.warn("Неизвестный параметр фильтрации state={}", state);
                return List.of();
        }
        return bookingMapper.toDto(result);
    }

    @Override
    public Collection<BookingDto> getBookingsForItems(Long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        List<Booking> result;
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByItemOwnerIdOrderByEndDesc(userId);
                break;
            case CURRENT:
                result = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                result = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(userId,
                        LocalDateTime.now());
                break;
            case FUTURE:
                result = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByEndDesc(userId,
                        LocalDateTime.now());
                break;
            case WAITING:
                result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByEndDesc(userId,
                        BookingStatus.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByEndDesc(userId,
                        BookingStatus.REJECTED);
                break;
            default:
                log.warn("Неизвестный параметр фильтрации state={}", state);
                return List.of();
        }
        return bookingMapper.toDto(result);
    }
}
