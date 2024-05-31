package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.nosql.AuthorN;
import ru.otus.hw.models.nosql.BookN;
import ru.otus.hw.models.nosql.GenreN;
import ru.otus.hw.models.sql.Book;

import javax.sql.DataSource;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class JobConfig {

    private static final int CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";


    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Bean
    public ItemReader<Book> reader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from Book b")
                .build();
    }

    //TODO надо решить проблему обращения к БД для каждой книги
    @Bean
    public ItemProcessor<Book, BookN> processor() {
        return book -> {
            String title = book.getTitle();

            AuthorN newAuthor = AuthorConverter.toAuthorN(book.getAuthor());
            Set<GenreN> newGenres = book.getGenres().stream().map(GenreConverter::toGenreN).collect(Collectors.toSet());
            return new BookN(title, newAuthor, newGenres);
        };
    }

    @Bean
    public ItemWriter<BookN> writer() {
        return new MongoItemWriterBuilder<BookN>()
                .collection("library")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Job importBookJob(Step step1) {
        return new JobBuilder(IMPORT_BOOK_JOB_NAME, jobRepository)
                .flow(step1)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Start job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("End job");
                    }
                })
                .build();
    }

    @Bean
    public Step step1(ItemReader<Book> reader, ItemWriter<BookN> writer, ItemProcessor<Book, BookN> processor) {
        return new StepBuilder("relocateBookStep", jobRepository)
                .<Book, BookN>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ItemReadListener<Book>() {
                    public void beforeRead() {
                        logger.info("Start read");
                    }

                    public void afterRead(Book book) {
                        logger.info("Book " + book.getTitle());
                    }

                    public void onReadError(Exception e) {
                        logger.info("Read error");
                    }
                })
                .build();
    }
}
