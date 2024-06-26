package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findForBook(String bookId) {
        List<Comment> commentList = commentRepository.findByBookId(bookId);
        return commentList.stream().map(CommentDtoConverter::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id)
                .map(CommentDtoConverter::toDto);
    }

    @Override
    @Transactional
    public CommentDto insert(String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        Comment comment = new Comment(null, text, book);
        return CommentDtoConverter.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(String id, String newText) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        comment.setText(newText);
        return CommentDtoConverter.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
