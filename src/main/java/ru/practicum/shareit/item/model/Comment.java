package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.shareit.utils.SqlHelper.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = TABLE_COMMENTS, schema = SCHEMA)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COMMENT_ID)
    private Long id;

    @Column(name = COMMENT_ITEM_ID, nullable = false)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COMMENT_AUTHOR_ID)
    private User author;

    @Column(name = COMMENT_AUTHOR_ID, insertable = false, updatable = false)
    private Long authorId;

    @Column(name = COMMENT_TEXT, nullable = false)
    private String text;

    @Column(name = COMMENT_CREATED, nullable = false)
    private LocalDateTime created;
}
