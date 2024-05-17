package ru.otus.hw.commands;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment for book", key = "fbc")
    public String findCommentsForBook(String id) {
        return commentService.findForBook(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by id", key = "cbi")
    public String findCommentById(String id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString).orElse("Comment with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Update comment", key = "uc")
    private String updateBook(String id, String newText) {
        return commentConverter.commentToString(commentService.update(id, newText));
    }

    @ShellMethod(value = "Insert comment", key = "ic")
    private String insertComment(String text, String bookId) {
        return commentConverter.commentToString(commentService.insert(text, bookId));
    }

    @ShellMethod(value = "Delete comment", key = "dc")
    private void deleteComment(String id) {
        commentService.deleteById(id);
    }
}
