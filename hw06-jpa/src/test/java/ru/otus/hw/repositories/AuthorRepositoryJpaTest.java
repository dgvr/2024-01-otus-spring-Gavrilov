package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    private static final long FIRST_AUTHOR_ID = 1L;
    private static final String FIRST_AUTHOR_NAME = "Author_1";

    @Autowired
    private AuthorRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен загружать список всех авторов")
    @Test
    void findAll() {
        List<Author> authorList = repositoryJpa.findAll();
        TypedQuery<Author> expectedQuery = em.getEntityManager().createQuery(
                "select a from Author a", Author.class);
        List<Author> expectedAuthorList = expectedQuery.getResultList();
        assertEquals(expectedAuthorList, authorList);
    }

    @DisplayName(" должен загружать информацию о нужном авторе по его id")
    @Test
    void findById() {
        val optionalActualAuthor = repositoryJpa.findById(FIRST_AUTHOR_ID);
        val expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void compareNameById() {
        Optional<Author> author = repositoryJpa.findById(FIRST_AUTHOR_ID);
        assertThat(author).isNotEmpty().get()
                .hasFieldOrPropertyWithValue("fullName", FIRST_AUTHOR_NAME);
    }
}