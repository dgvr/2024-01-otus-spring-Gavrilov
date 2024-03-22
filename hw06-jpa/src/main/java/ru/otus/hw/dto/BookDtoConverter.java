package ru.otus.hw.dto;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDtoConverter {

    public static BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        Author author = book.getAuthor();
        if (author != null) {
            bookDto.setAuthor(new AuthorDto(author.getId(), author.getFullName()));
        }
        if (book.getGenres() != null) {
            List<GenreDto> genreDtoList = new ArrayList<>(book.getGenres().size());
            bookDto.setGenres(genreDtoList);
            book.getGenres().forEach(genre -> {
                genreDtoList.add(new GenreDto(genre.getId(), genre.getName()));

            });
        }
        return bookDto;
    }
}
