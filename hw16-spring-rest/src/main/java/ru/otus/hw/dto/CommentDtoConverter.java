package ru.otus.hw.dto;

import ru.otus.hw.models.Comment;

public class CommentDtoConverter {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
