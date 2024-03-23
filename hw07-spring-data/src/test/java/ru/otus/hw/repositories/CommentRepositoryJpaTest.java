package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {

    private static final long FIRST_COMMENT_ID = 1L;

    private static final long NONEXISTENT_COMMENT_ID = 7L;

    private static final long BOOK_ID = 1L;

    private static final int COMMENTS_FOR_BOOK_COUNT = 2;

    @Autowired
    private CommentRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @Test
    void findByBook() {
        List<Comment> commentList = repositoryJpa.findByBook(BOOK_ID);

        TypedQuery<Comment> expectedQuery = em.getEntityManager().createQuery(
                "select c from Comment c join c.book b where b.id = :bookId", Comment.class);
        expectedQuery.setParameter("bookId", BOOK_ID);
        List<Comment> expectedCommens = expectedQuery.getResultList();
        assertEquals(expectedCommens, commentList);
    }

    @Test
    void findById() {
        val optionalActualComment = repositoryJpa.findById(FIRST_COMMENT_ID);
        val expectedAuthor = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(optionalActualComment).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }


    @Test
    void findAllByBook() {
        List<Comment> comments = repositoryJpa.findByBook(BOOK_ID);
        assertThat(comments).hasSize(COMMENTS_FOR_BOOK_COUNT);
    }


    @Test
    void save() {
        Comment comment = em.find(Comment.class, NONEXISTENT_COMMENT_ID);
        assertThat(comment).isNull();
        comment = new Comment(0, "newText", new Book());
        repositoryJpa.save(comment);
        comment = em.find(Comment.class, NONEXISTENT_COMMENT_ID);
        assertThat(comment).hasFieldOrPropertyWithValue("id", NONEXISTENT_COMMENT_ID);
    }

    @Test
    void deleteById() {
        Comment comment = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(comment).isNotNull();
        repositoryJpa.deleteById(FIRST_COMMENT_ID);
        comment = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(comment).isNull();
    }
}