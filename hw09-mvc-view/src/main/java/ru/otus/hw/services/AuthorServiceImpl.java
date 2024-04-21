package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.AuthorDtoConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        List<Author> authorList = authorRepository.findAll();
        return authorList.stream().map(AuthorDtoConverter::toDto).toList();
    }

    @Override
    public Optional<AuthorDto> findById(long id) {
        return authorRepository.findById(id)
                .map(AuthorDtoConverter::toDto);
    }
}
