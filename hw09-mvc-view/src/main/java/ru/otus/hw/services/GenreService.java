package ru.otus.hw.services;

import ru.otus.hw.dto.GenreDto;

import java.util.Collection;
import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();

    List<GenreDto> findByIdIn(Collection<Long> ids);
}
