package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    CommentRepository commentRepository;

    //@Test
    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    public void test(Long bookId) {
        TypedQuery<Comment> expectedQuery = entityManager.getEntityManager().createQuery(
                "select c from Comment c join c.book b where b.id = :bookId", Comment.class);
        expectedQuery.setParameter("bookId", bookId);
        List<Comment> expectedComments = expectedQuery.getResultList();
        List<Comment> comments = commentRepository.findByBookId(bookId);
        assertThat(comments).hasSameElementsAs(expectedComments);
    }
}
