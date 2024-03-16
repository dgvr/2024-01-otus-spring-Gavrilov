package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> findForBook(long bookId);

    Optional<Comment> findById(long id);

    Comment insert(String text, long bookId);

    Comment update(long id, String newText);

    void deleteById(long id);
}
