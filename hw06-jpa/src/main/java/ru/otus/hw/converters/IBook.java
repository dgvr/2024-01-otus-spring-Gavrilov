package ru.otus.hw.converters;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface IBook {

    long getId();

    String getTitle();

    Author getAuthor();

    List<Genre> getGenres();

    List<Comment> getComments();
}
