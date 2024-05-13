package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Authority;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    List<Authority> findAllByUsername(String username);
}
