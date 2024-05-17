package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookDto {

    private String id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;
}
