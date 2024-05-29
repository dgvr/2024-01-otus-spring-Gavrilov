package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoOperations;

    @Test
    public void findByIdInTest() {

        Genre genre1 = mongoOperations.save(new Genre("1", "Genre1"));
        Genre genre2 = mongoOperations.save(new Genre("2", "Genre2"));
        Genre genre3 = mongoOperations.save(new Genre("3", "Genre3"));

        List<Genre> genreList = genreRepository.findByIdIn(List.of("2", "3"));
        assertThat(genreList)
                .containsExactlyElementsOf(List.of(genre2, genre3));
    }
}
