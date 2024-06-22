package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = "author")
    Optional<Book> findById(long id);

    @Nonnull
    @EntityGraph(attributePaths = "author")
    List<Book> findAll();
}
