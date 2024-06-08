package ru.otus.hw.actuators;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class ApplicationHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;


    @Override
    public Health health() {
        long bookCount = bookRepository.count();
        return Health.up().withDetail("message", String.format("Осталось книг в библиотеке: %d", bookCount)).build();
    }
}
