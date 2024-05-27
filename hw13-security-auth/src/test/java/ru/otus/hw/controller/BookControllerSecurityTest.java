package ru.otus.hw.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @ParameterizedTest
    @MethodSource("testArguments")
    void testRight(String userName, String role, ResultMatcher matcher, MockHttpServletRequestBuilder builder) throws Exception {
        mvc.perform(builder.with(user(userName).roles(role)))
                .andExpect(matcher);
    }

    @ParameterizedTest
    @MethodSource("testArgumentsNoAuthorized")
    void testRightNoAuthorized(MockHttpServletRequestBuilder builder) throws Exception {
        mvc.perform(builder)
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    static Stream<? extends Arguments> testArguments() {
        return Stream.of(
                Arguments.of(
                        "admin",
                        "ADMIN",
                        status().isOk(),
                        get("/")

                ),
                Arguments.of(
                        "user",
                        "USER",
                        status().isOk(),
                        get("/")

                ),
                Arguments.of(
                        "user",
                        "USER",
                        status().isOk(),
                        get("/book/edit/title/{id}", 1)
                ),

                Arguments.of(
                        "user",
                        "USER",
                        status().isOk(),
                        get("/book/edit/author")
                                .param("bookId", "1")
                ),

                Arguments.of(
                        "user",
                        "USER",
                        status().isOk(),
                        get("/book/edit/genre")
                                .param("bookId", "1")
                ),

                Arguments.of(
                        "user",
                        "USER",
                        status().isOk(),
                        get("/book/about/{id}", 1)
                ),

                Arguments.of(
                        "admin",
                        "ADMIN",
                        status().isOk(),
                        get("/book/create")
                ),
                Arguments.of(
                        "user",
                        "USER",
                        status().isOk(),
                        get("/book/create")
                ),

                Arguments.of(
                        "user",
                        "USER",
                        status().isForbidden(),
                        post("/book/create")
                                .param("title", "bookTitle").param("authorId", "1").param("genreIds", "1", "2", "3")
                                .with(csrf())
                ),
                Arguments.of(
                        "user",
                        "USER",
                        status().isForbidden(),
                        post("/book/edit/all/{id}", 1).param("action", "delete")
                                .with(csrf())
                )
        );
    }


    static Stream<? extends Arguments> testArgumentsNoAuthorized() {
        return Stream.of(
                Arguments.of(
                        post("/book/edit/title/{id}", 1)
                                .param("title", "titleBook")
                                .with(csrf())
                ),
                Arguments.of(
                        post("/book/edit/author")
                                .param("authorId", "1").param("bookId", "1")
                                .with(csrf())
                ),
                Arguments.of(
                        post("/book/edit/genre")
                                .param("bookId", "1").param("genreIds", "1", "2", "3")
                                .with(csrf())
                ),
                Arguments.of(
                        post("/book/create")
                                .param("title", "bookTitle").param("authorId", "1").param("genreIds", "1", "2", "3")
                                .with(csrf())
                ),
                Arguments.of(
                        post("/book/edit/all/{id}", 1).param("action", "delete")
                                .with(csrf())
                ),
                Arguments.of(
                        get("/")

                ),
                Arguments.of(
                        get("/book/edit/title/{id}", 1)
                ),
                Arguments.of(
                        get("/book/edit/author")
                                .param("bookId", "1")
                ),
                Arguments.of(
                        get("/book/edit/genre")
                                .param("bookId", "1")
                ),
                Arguments.of(
                        get("/book/about/{id}", 1)
                ),
                Arguments.of(
                        get("/book/create")
                )
        );
    }
}