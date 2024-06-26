package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id)
                .map(BookDtoConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        List<Book> bookList = bookRepository.findAll();
        return bookList.stream().map(BookDtoConverter::toDto).toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        return BookDtoConverter.toDto(save(0, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        return BookDtoConverter.toDto(save(id, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public BookDto update(BookDto bookDto) {
        Set<Long> genreIds = bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet());
        return BookDtoConverter.toDto(save(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor().getId(), genreIds));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateTitle(long id, String title) {
        BookDto bookDto = findById(id)
                .orElseThrow(jakarta.persistence.EntityNotFoundException::new);
        bookDto.setTitle(title);
        update(bookDto);
    }

    @Override
    @Transactional
    public void updateAuthor(long id, long authorId) {
        BookDto bookDto = findById(id)
                .orElseThrow(jakarta.persistence.EntityNotFoundException::new);
        AuthorDto authorDto = authorService.findById(authorId)
                .orElseThrow(jakarta.persistence.EntityNotFoundException::new);
        bookDto.setAuthor(authorDto);
        update(bookDto);
    }

    @Override
    @Transactional
    public void updateGenre(long id, Collection<Long> genreIds) {
        BookDto bookDto = findById(id)
                .orElseThrow(jakarta.persistence.EntityNotFoundException::new);
        List<GenreDto> genreDtos = genreService.findByIdIn(genreIds);
        bookDto.setGenres(genreDtos);
        update(bookDto);

    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var comments = commentRepository.findByBookId(id);
        var book = new Book(id, title, author, genres, comments);
        return bookRepository.save(book);
    }
}
