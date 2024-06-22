package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<CommentDto> findForBook(long bookId);

    Optional<CommentDto> findById(long id);

    CommentDto insert(String text, long bookId);

    CommentDto update(long id, String newText);

    void deleteById(long id);
}
