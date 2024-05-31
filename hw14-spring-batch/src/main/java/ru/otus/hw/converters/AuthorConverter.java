package ru.otus.hw.converters;

import ru.otus.hw.models.nosql.AuthorN;
import ru.otus.hw.models.sql.Author;

public class AuthorConverter {

    public static AuthorN toAuthorN(Author author) {
        return new AuthorN(author.getFullName());
    }
}
