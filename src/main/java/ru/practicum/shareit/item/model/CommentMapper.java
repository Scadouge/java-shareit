package ru.practicum.shareit.item.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "id", source = "commentDto.id")
    Comment toModel(CommentDto commentDto, User author, Long itemId);

    @Mapping(target = "authorName", source = "comment.author.name")
    CommentDto toDto(Comment comment);

    @Mapping(target = "authorName", source = "comment.author.name")
    List<CommentDto> toDto(List<Comment> comment);
}
