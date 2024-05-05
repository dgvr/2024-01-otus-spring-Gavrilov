package ru.otus.hw.dto;

import ru.otus.hw.models.Author;

public class AuthorDtoConverter {

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
