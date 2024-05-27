package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAllowedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        Item item = itemMapper.toModel(itemDto, userId);
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem);
    }

    @Override
    public ItemExtendDto get(Long id, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        Item item = itemRepository.findById(id, Item.class).orElseThrow(() -> new NotFoundException(id));
        Boolean withBooking = Objects.equals(userId, item.getOwnerId());
        return getItemsExtendedInfo(List.of(item), withBooking).stream().findFirst()
                .orElseThrow(() -> new NotFoundException(id));
    }

    @Override
    public Collection<ItemExtendDto> getAll(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        List<Item> items = itemRepository.findAllByOwnerId(userId, Item.class);
        return getItemsExtendedInfo(items, true);
    }

    private Collection<ItemExtendDto> getItemsExtendedInfo(List<Item> items, Boolean withBooking) {
        Set<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toSet());
        Map<Long, List<Comment>> comments = commentRepository.findAllByItemIdIn(itemIds, Comment.class).stream()
                .collect(Collectors.groupingBy(Comment::getItemId));
        Map<Long, List<Booking>> bookings = null;
        if (withBooking) {
            bookings = bookingRepository.findAllByItemIdIn(itemIds).stream()
                    .collect(Collectors.groupingBy(Booking::getItemId));
        }
        Collection<ItemExtendDto> itemExtendDtos = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Item item : items) {
            Booking last = null;
            Booking next = null;
            List<Comment> commentsList = new ArrayList<>();
            if (comments.get(item.getId()) != null) {
                commentsList = comments.get(item.getId());
            }
            if (bookings != null && bookings.get(item.getId()) != null) {
                LinkedList<Booking> collect = bookings.get(item.getId()).stream()
                        .sorted(Comparator.comparing(Booking::getEnd)).collect(Collectors.toCollection(LinkedList::new));
                for (Booking booking : collect) {
                    if (booking.getStatus() == BookingStatus.REJECTED) {
                        continue;
                    }
                    if ((booking.getEnd().isBefore(now))
                            || booking.getStart().isBefore(now) && booking.getEnd().isAfter(now)) {
                        last = booking;
                    } else {
                        next = booking;
                    }
                    if (last != null && next != null) {
                        break;
                    }
                }
            }
            itemExtendDtos.add(itemMapper.toItemBookingInfoDto(item, last, next, commentMapper.toDto(commentsList)));
        }
        return itemExtendDtos.stream().sorted(Comparator.comparing(ItemExtendDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        Item savedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId));
        if (!Objects.equals(userId, savedItem.getOwnerId())) {
            throw new NotAllowedException("Изменять предмет может только владелец");
        }
        itemMapper.updateModel(savedItem, itemDto);
        return itemMapper.toDto(itemRepository.save(savedItem));
    }

    @Override
    public Collection<ItemDto> searchAvailableItems(String text) {
        return itemRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text,
                text, true, ItemDto.class);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        Comment comment = commentRepository.findByAuthorIdAndItemId(userId, itemId, Comment.class);
        if (comment != null) {
            throw new ValidationException("Отзыв уже существует");
        }
        List<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now());
        bookings.stream().filter(b -> b.getStatus() == BookingStatus.APPROVED).findFirst().orElseThrow(() ->
                new ValidationException("Пользователь не бронировал этот предмет для оставления отзывов"));
        Comment newComment = commentMapper.toModel(commentDto, user, itemId);
        newComment.setCreated(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(newComment));
    }
}
