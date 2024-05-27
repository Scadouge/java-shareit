package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static ru.practicum.shareit.utils.SqlHelper.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = TABLE_USERS, schema = SCHEMA)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_ID)
    private Long id;

    @Column(name = USER_NAME, nullable = false)
    private String name;

    @Column(name = USER_EMAIL, nullable = false, unique = true)
    private String email;
}
