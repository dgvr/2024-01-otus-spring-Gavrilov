package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class GenreRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void findByIdInTest() {
        List<Long> testIds = Arrays.asList(1L, 3L);
        Genre genre1 = entityManager.find(Genre.class, testIds.get(0));
        Genre genre3 = entityManager.find(Genre.class, testIds.get(1));
        List<Genre> expectedList = Arrays.asList(genre1, genre3);

        List<Genre> genreList = genreRepository.findByIdIn(testIds);
        assertThat(genreList).hasSize(testIds.size());
        assertThat(genreList).hasSameElementsAs(expectedList);
    }
}
