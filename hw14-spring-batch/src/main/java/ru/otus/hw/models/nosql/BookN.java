package ru.otus.hw.models.nosql;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;


@Getter
@Setter
@Document(collection = "Books")
public class BookN {

    @Id
    ObjectId objectId;

    private String title;

    private AuthorN author;

    private Set<GenreN> genres;

    public BookN(String title, AuthorN author, Set<GenreN> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }
}
