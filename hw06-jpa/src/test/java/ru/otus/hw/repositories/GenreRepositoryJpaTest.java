package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {

    private static final int GENRE_COUNT = 6;

    @Autowired
    private GenreRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @Test
    void findAll() {
        List<Genre> genreList = repositoryJpa.findAll();
        TypedQuery<Genre> expectedQuery = em.getEntityManager().createQuery(
                "select g from Genre g", Genre.class);
        List<Genre> expectedGenreList = expectedQuery.getResultList();
        assertEquals(expectedGenreList, genreList);
    }

    @Test
    void findAllByIds() {
        Set<Long> findIds = Set.of(1L, 3L, 5L);
        List<Genre> genreList = repositoryJpa.findAllByIds(findIds);
        TypedQuery<Genre> expectedQuery = em.getEntityManager().createQuery(
                "select g from Genre g where g.id in (:ids)", Genre.class
        );
        expectedQuery.setParameter("ids", findIds);
        List<Genre> expectedList = expectedQuery.getResultList();
        assertEquals(expectedList, genreList);
    }

    @Test
    @DisplayName("возвращать список всех жанров")
    void calcCountGenres() {
        List<Genre> genres = repositoryJpa.findAll();
        assertThat(genres).hasSize(GENRE_COUNT);
    }
}