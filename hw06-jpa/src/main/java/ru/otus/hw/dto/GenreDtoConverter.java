package ru.otus.hw.dto;

import ru.otus.hw.models.Genre;

public class GenreDtoConverter {

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
