package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<CommentDto> findForBook(String bookId);

    Optional<CommentDto> findById(String id);

    CommentDto insert(String text, String bookId);

    CommentDto update(String id, String newText);

    void deleteById(String id);
}
