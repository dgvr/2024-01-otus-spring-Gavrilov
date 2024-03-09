package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sql = "select books.id as bookId, books.title as bookTitle, authors.id as authorId, " +
                "authors.full_name as authorName, genres.id as genreId, genres.name as genreName from books " +
                "left join authors on books.author_id = authors.id " +
                "left join books_genres jt on books.id = jt.book_id " +
                "left join genres on genres.id = jt.genre_id " +
                "where books.id = :id";
        Book book = namedParameterJdbcOperations.query(sql, params, new BookResultSetExtractor());
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sql = "delete from books where id = :id";
        namedParameterJdbcOperations.update(sql, params);
        removeGenresRelationsFor(id);
    }

    private List<Book> getAllBooksWithoutGenres() {
        String sql = "select books.id as bookId, books.title, authors.id as authorId, authors.full_name as authorName " +
                "from books left join authors on books.author_id = authors.id";
        return namedParameterJdbcOperations.query(sql, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        String sql = "select book_id, genre_id from books_genres";
        return namedParameterJdbcOperations.query(sql, new DataClassRowMapper<>(BookGenreRelation.class));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Book> booksMap = booksWithoutGenres.stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        Map<Long, Genre> genresMap = genres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));

        relations.forEach(relation -> {
            Book book = booksMap.get(relation.bookId());
            Genre genre = genresMap.get(relation.genreId());
            if (book.getGenres() == null) {
                book.setGenres(new ArrayList<>());
            }
            book.getGenres().add(genre);
        });
    }

    private Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());

        var keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcOperations.update(
                "insert into books(title, author_id) values (:title, :author_id)",
                params, keyHolder, new String[]{"id"});
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        Long authorId = book.getAuthor() != null ? book.getAuthor().getId() : null;
        params.addValue("author_id", authorId);

        String sql = "update books set title = :title, author_id = :author_id where id = :id";
        int updatedRows = namedParameterJdbcOperations.update(sql, params);
        if (updatedRows == 0) {
            throw new EntityNotFoundException(String.format("Do not update Book with id = %d", book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        Long bookId = book.getId();
        List<Long> genreIds = book.getGenres().stream().map(Genre::getId).toList();

        namedParameterJdbcOperations.getJdbcOperations().batchUpdate(
                "insert into books_genres(book_id, genre_id) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, bookId);
                        ps.setLong(2, genreIds.get(i));
                    }

                    public int getBatchSize() {
                        return genreIds.size();
                    }
                }
        );
    }

    private void removeGenresRelationsFor(Book book) {
        removeGenresRelationsFor(book.getId());
    }

    private void removeGenresRelationsFor(long bookId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookId", bookId);
        namedParameterJdbcOperations.update(
                "delete from books_genres where book_id = :bookId", params
        );
    }


    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("bookId");
            String bookTitle = rs.getString("title");

            Author author = null;
            long authorId = rs.getLong("authorId");
            if (!rs.wasNull()) {
                String authorName = rs.getString("authorName");
                author = new Author(authorId, authorName);
            }
            return new Book(bookId, bookTitle, author, null);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;

            while (rs.next()) {
                if (book == null) {
                    book = new Book();
                    book.setId(rs.getLong("bookId"));
                    book.setTitle(rs.getString("bookTitle"));
                    book.setAuthor(getAuthor(rs));
                }
                Genre genre = getGenre(rs);
                addGenreToBook(book, genre);
            }
            return book;
        }

        private Author getAuthor(ResultSet rs) throws SQLException {
            long authorId = rs.getLong("authorId");
            if (rs.wasNull()) {
                return null;
            }
            String authorName = rs.getString("authorName");
            return new Author(authorId, authorName);
        }

        private Genre getGenre(ResultSet rs) throws SQLException {
            long genreId = rs.getLong("genreId");
            if (rs.wasNull()) {
                return null;
            }
            String genreName = rs.getString("genreName");
            return new Genre(genreId, genreName);
        }

        private void addGenreToBook(Book book, Genre genre) {
            if (genre == null) {
                return;
            }
            if (book.getGenres() == null) {
                book.setGenres(new ArrayList<>());
            }
            book.getGenres().add(genre);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
