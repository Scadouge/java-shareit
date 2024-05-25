package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static ru.practicum.shareit.utils.SqlHelper.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = TABLE_ITEMS, schema = SCHEMA)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ITEM_ID)
    private Long id;

    @Column(name = ITEM_NAME, nullable = false)
    private String name;

    @Column(name = ITEM_DESCRIPTION, nullable = false)
    private String description;

    @Column(name = ITEM_AVAILABLE, nullable = false)
    private Boolean available;

    @Column(name = ITEM_OWNER_ID, nullable = false)
    private Long ownerId;

    @Column(name = ITEM_REQUEST_ID)
    private Long requestId;
}
