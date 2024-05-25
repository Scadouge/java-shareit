package ru.practicum.shareit.item.storage;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Fetch(FetchMode.JOIN)
    <T> T findByAuthorIdAndItemId(Long authorId, Long itemId, Class<T> type);

    @Fetch(FetchMode.JOIN)
    <T> List<T> findAllByItemIdIn(Set<Long> itemIds, Class<T> type);
}
