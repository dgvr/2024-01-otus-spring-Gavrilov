package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class CommentRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void findByBookIdTest() {
        Book book = new Book("2", "Book 2", null, null);
        mongoTemplate.save(book);

        Comment expectedComment = mongoTemplate.save(new Comment("1", "Comment 1", book));

        List<Comment> comments = commentRepository.findByBookId("2");
        assertThat(comments).hasSize(1);
        Comment comment = comments.get(0);
        assertThat(comment)
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
    }

    @Test
    public void deleteByBookIdTest() {
        Book book1 = new Book("1", "Book 1", null, null);
        mongoTemplate.save(book1);

        Book book2 = new Book("2", "Book 2", null, null);
        mongoTemplate.save(book2);

        Comment comment1 = mongoTemplate.save(new Comment("1", "Comment 1", book1));
        Comment comment2 = mongoTemplate.save(new Comment("2", "Comment 2", book2));
        Comment comment3 = mongoTemplate.save(new Comment("3", "Comment 3", book2));

        commentRepository.deleteByBookId("2");

        List<Comment> comments = commentRepository.findAll();

        assertThat(comments).hasSize(1);

        Comment comment = comments.get(0);
        assertThat(comment)
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(comment1);
    }
}
