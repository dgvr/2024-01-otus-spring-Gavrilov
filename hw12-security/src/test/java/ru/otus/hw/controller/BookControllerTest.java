package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;


    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;


    @WithMockUser
    @Test
    void listBook() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andExpect(model().attribute("books", hasSize(0)));
    }

    @WithMockUser
    @Test
    void editBookTitle() throws Exception {
        BookDto bookDto = createEmptyBookDto();
        given(bookService.findById(anyLong())).willReturn(Optional.of(bookDto));
        mvc.perform(get("/book/edit/title/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("editBookTitle"))
                .andExpect(model().attribute("book", is(bookDto)));
    }

    @WithMockUser
    @Test
    void updateTitle() throws Exception {
        BookDto bookDto = createEmptyBookDto();
        given(bookService.findById(anyLong())).willReturn(Optional.of(bookDto));

        mvc.perform(post("/book/edit/title/{id}", 1)
                        .param("title", "titleBook")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/about/1"));
    }

    @WithMockUser
    @Test
    void editBookAuthor() throws Exception {
        BookDto bookDto = createEmptyBookDto();
        bookDto.setAuthor(new AuthorDto(1, "name"));
        given(bookService.findById(anyLong())).willReturn(Optional.of(bookDto));
        mvc.perform(get("/book/edit/author")
                        .param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editBookAuthor"))
                .andExpect(model().attribute("authors", hasSize(0)));
    }

    @WithMockUser(
            username = "us"
    )
    @Test
    void updateAuthor() throws Exception {
        BookDto bookDto = createEmptyBookDto();
        given(bookService.findById(anyLong())).willReturn(Optional.of(bookDto));
        given(authorService.findById(anyLong())).willReturn(Optional.of(new AuthorDto(1, "authorName")));

        mvc.perform(post("/book/edit/author")
                        .param("authorId", "1").param("bookId", "1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/about/1"));
    }

    @WithMockUser
    @Test
    void editBookGenre() throws Exception {
        BookDto bookDto = createEmptyBookDto();
        bookDto.setAuthor(new AuthorDto(1, "name"));
        given(bookService.findById(anyLong())).willReturn(Optional.of(bookDto));
        mvc.perform(get("/book/edit/genre")
                        .param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editBookGenre"))
                .andExpect(model().attribute("genres", hasSize(0)));
    }

    @WithMockUser
    @Test
    void updateGenre() throws Exception {
        BookDto bookDto = createEmptyBookDto();
        given(bookService.findById(anyLong())).willReturn(Optional.of(bookDto));

        mvc.perform(post("/book/edit/genre")
                        .param("bookId", "1").param("genreIds", "1", "2", "3")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/about/1"));
    }

    @WithMockUser
    @Test
    void bookAbout() throws Exception {
        BookDto bookDto = createEmptyBookDto();
        bookDto.setAuthor(new AuthorDto(1, "authorName"));
        given(bookService.findById(anyLong())).willReturn(Optional.of(bookDto));
        mvc.perform(get("/book/about/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bookAbout"))
                .andExpect(model().attribute("book", is(bookDto)))
                .andExpect(model().attribute("mode", "about"));
    }

    @WithMockUser
    @Test
    void createBook() throws Exception {
        mvc.perform(get("/book/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createBook"))
                .andExpect(model().attribute("authors", hasSize(0)))
                .andExpect(model().attribute("genres", hasSize(0)));
    }

    @WithMockUser
    @Test
    void testCreateBook() throws Exception {
        mvc.perform(post("/book/create")
                        .param("title", "bookTitle").param("authorId", "1").param("genreIds", "1", "2", "3")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }

    @WithMockUser
    @Test
    void deleteBook() throws Exception {
        mvc.perform(post("/book/edit/all/{id}", 1).param("action", "delete")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }

    private BookDto createEmptyBookDto() {
        return new BookDto();
    }
}