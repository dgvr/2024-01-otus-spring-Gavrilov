package ru.otus.hw.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listBook(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "bookList";
    }


    @GetMapping("/book/edit/title/{id}")
    public String editBookTitle(@PathVariable(value = "id") long id, Model model) {
        BookDto book = bookService.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("book", book);
        return "editBookTitle";
    }

    @PostMapping("/book/edit/title/{id}")
    public String updateTitle(
            @PathVariable(value = "id") long id,
            @RequestParam String title
    ) {
        BookDto bookDto = bookService.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        bookDto.setTitle(title);
        bookService.update(bookDto);
        return "redirect:/book/about/{id}";
    }

    @GetMapping(value = "/book/edit/author")
    public String editBookAuthor(@RequestParam("bookId") long bookId, Model model) {
        BookDto book = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("bookId", bookId);
        model.addAttribute("authorId", book.getAuthor().getId());
        List<AuthorDto> authorsDto = authorService.findAll();
        model.addAttribute("authors", authorsDto);
        return "editBookAuthor";
    }

    @PostMapping(value = "/book/edit/author")
    public String updateAuthor(
            @RequestParam long bookId,
            @RequestParam long authorId
    ) {
        BookDto bookDto = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        AuthorDto authorDto = authorService.findById(authorId)
                .orElseThrow(EntityNotFoundException::new);
        bookDto.setAuthor(authorDto);
        bookService.update(bookDto);
        return "redirect:/book/about/" + bookId;
    }

    @GetMapping("/book/edit/genre")
    public String editBookGenre(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        List<GenreDto> genresDto = genreService.findAll();
        model.addAttribute("genres", genresDto);
        return "editBookGenre";
    }

    @PostMapping("/book/edit/genre")
    public String updateGenre(
            @RequestParam long bookId,
            @RequestParam(required = false) List<Long> genreIds
    ) {
        BookDto bookDto = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        List<GenreDto> genreDtos = genreService.findByIdIn(genreIds);
        bookDto.setGenres(genreDtos);
        bookService.update(bookDto);
        return "redirect:/book/about/" + bookId;
    }

    @GetMapping("/book/about/{id}")
    public String bookAbout(@PathVariable(value = "id") long id, Model model) {
        BookDto book = bookService.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("book", book);
        model.addAttribute("mode", "about");
        return "bookAbout";
    }

    @GetMapping("/book/create")
    public String createBook(Model model) {
        List<AuthorDto> authorsDto = authorService.findAll();
        model.addAttribute("authors", authorsDto);
        List<GenreDto> genresDto = genreService.findAll();
        model.addAttribute("genres", genresDto);
        return "createBook";
    }

    @PostMapping("/book/create")
    public String createBook(
            @RequestParam String title,
            @RequestParam long authorId,
            @RequestParam(required = false) Set<Long> genreIds
    ) {
        bookService.update(0, title, authorId, genreIds);
        return "redirect:/";
    }

    @PostMapping(value = "/book/edit/all/{id}", params = "action=delete")
    public String deleteBook(@PathVariable(value = "id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView entityNotFound(RuntimeException ex) {
        ModelAndView modelAndView = new ModelAndView("/errorPage");
        modelAndView.addObject("errMessage", ex.getMessage());
        return modelAndView;
    }
}
