package ru.otus.hw.rest;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoFront;
import ru.otus.hw.services.BookService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;


    @GetMapping("/api/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/book/{id}")
    public BookDto getBook(@PathVariable(value = "id") long id) {
        return bookService.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping("/api/book")
    public BookDto saveBook(@RequestBody BookDtoFront bookDto) {
        return bookService.insert(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body("Таких тут нет!");
    }

}
