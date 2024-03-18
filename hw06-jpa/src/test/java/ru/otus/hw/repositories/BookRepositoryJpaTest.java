package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final String OLD_BOOK_TITLE = "BookTitle_1";

    private static final String NEW_BOOK_TITLE = "newBookTitle";

    private static final int BOOK_COUNT = 3;

    @Autowired
    private BookRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @Test
    void findById() {
        Optional<Book> book = repositoryJpa.findById(FIRST_BOOK_ID);
        assertThat(book).isNotEmpty().get()
                .hasFieldOrPropertyWithValue("title", OLD_BOOK_TITLE);
    }

    @Test
    void findAll() {
        List<Book> bookList = repositoryJpa.findAll();
        assertThat(bookList).hasSize(BOOK_COUNT);
    }

    @Test
    void save() {
        Book book = em.find(Book.class, FIRST_BOOK_ID);
        book.setTitle(NEW_BOOK_TITLE);
        repositoryJpa.save(book);
        book = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(book).hasFieldOrPropertyWithValue("title", NEW_BOOK_TITLE);
    }

    @Test
    void deleteById() {
        Book book = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(book).isNotNull();
        repositoryJpa.deleteById(FIRST_BOOK_ID);
        book = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(book).isNull();
    }
}