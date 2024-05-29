package ru.otus.hw.converters;

import ru.otus.hw.models.nosql.GenreN;
import ru.otus.hw.models.sql.Genre;

public class GenreConverter {


    public static GenreN toGenreN(Genre genre) {
        return new GenreN(genre.getName());
    }
}
