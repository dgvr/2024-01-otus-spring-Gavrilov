package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookDtoFront {

    private long id;

    private String title;

    private long author;

    private Set<Long> genres;
}
