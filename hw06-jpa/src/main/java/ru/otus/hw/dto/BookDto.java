package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.converters.IBook;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

@Getter
@Setter
public class BookDto implements IBook {

    private long id;

    private String title;

    private Author author;

    private List<Genre> genres;

    private List<Comment> comments;

    public BookDto(Book book) {
        //FixMe Без вызова размера листа- LazyInitializationException. Видимо ДТО надо создавать как то иначе
        book.getGenres().size();
        book.getComments().size();
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.genres = book.getGenres();
        this.comments = book.getComments();
    }
}
