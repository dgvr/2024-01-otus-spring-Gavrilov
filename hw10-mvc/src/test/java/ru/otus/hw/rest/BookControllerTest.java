package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoConverter;
import ru.otus.hw.dto.BookDtoFront;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
class BookControllerTest {

    public static final String ERROR_STRING = "Таких тут нет!";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @Test
    void getAllBooks() throws Exception {
        Book book1 = createBook(1, "BookTitle_1", 1,
                "Author_1", Map.of(1L, "Genre_1", 2L, "Genre_2"));

        Book book2 = createBook(2, "BookTitle_2", 2,
                "Author_2", Map.of(3L, "Genre_3", 4L, "Genre_4"));

        Book book3 = createBook(3, "BookTitle_3", 3,
                "Author_3", Map.of(5L, "Genre_5", 6L, "Genre_6"));


        List<BookDto> expectedResult = List.of(BookDtoConverter.toDto(book1),
                BookDtoConverter.toDto(book2), BookDtoConverter.toDto(book3));

        given(bookService.findAll()).willReturn(expectedResult);

        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    void getBook() throws Exception {
        Book book = createBook(1, "BookTitle_1", 1,
                "Author_1", Map.of(1L, "Genre_1", 2L, "Genre_2"));

        BookDto expectedResult = BookDtoConverter.toDto(book);
        given(bookService.findById(1L)).willReturn(Optional.of(expectedResult));

        mvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    void shouldReturnExpectedErrorWhenBookNotFound() throws Exception {
        given(bookService.findById(1L)).willReturn(Optional.empty());

        mvc.perform(get("/api/book/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR_STRING));

    }

    @Test
    void saveBook() throws Exception {
        Book book = createBook(1, "BookTitle_1", 1,
                "Author_1", Map.of(1L, "Genre_1", 2L, "Genre_2"));
        BookDto bookDto = BookDtoConverter.toDto(book);
        given(bookService.insert(anyString(), anyLong(), anySet())).willReturn(bookDto);
        String expectedResult = mapper.writeValueAsString(bookDto);

        BookDtoFront bookDtoFront = new BookDtoFront();
        bookDtoFront.setId(bookDto.getId());
        bookDtoFront.setAuthor(bookDto.getAuthor().getId());
        bookDtoFront.setTitle(bookDto.getTitle());
        Set<Long> genreIds = new HashSet<>();
        bookDto.getGenres().forEach(genre -> {
            genreIds.add(genre.getId());
        });
        bookDtoFront.setGenres(genreIds);

        mvc.perform(post("/api/book").contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookDtoFront)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    private Book createBook(long id, String title, long authorId, String authorName, Map<Long, String> genresData) {

        Author author = new Author(authorId, authorName);
        List<Genre> genres = new ArrayList<>(genresData.size());

        genresData.forEach((genreId, genreName) -> {
            Genre genre = new Genre(genreId, genreName);
            genres.add(genre);

        });
        return new Book(id, title, author, genres, null);
    }
}