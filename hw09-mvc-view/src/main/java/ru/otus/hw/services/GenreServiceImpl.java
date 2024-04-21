package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.GenreDtoConverter;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        List<Genre> genreList = genreRepository.findAll();
        return genreList.stream().map(GenreDtoConverter::toDto).toList();
    }

    @Override
    @Transactional
    public List<GenreDto> findByIdIn(Collection<Long> ids) {
        List<Genre> genreList = genreRepository.findByIdIn(ids);
        return genreList.stream().map(GenreDtoConverter::toDto).toList();
    }
}
