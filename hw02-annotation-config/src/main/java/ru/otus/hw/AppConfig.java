package ru.otus.hw;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

//@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class AppConfig {

    //Сделал и через ComponentScan и через Java @Bean(закомментил этот вариант, оставил на память)
//    @Bean
//    IOService ioService() {
//        return new StreamsIOService(System.out, System.in);
//    }
//    @Bean
//    StudentService studentService() {
//        return new StudentServiceImpl(ioService());
//    }
//
//    @Bean
//    TestConfig testConfig() {
//        return new AppProperties();
//    }
//
//    @Bean
//    ResultService resultService() {
//        return new ResultServiceImpl(testConfig(), ioService());
//    }
//
//    @Bean
//    TestFileNameProvider testFileNameProvider() {
//        return new AppProperties();
//    }
//
//    @Bean
//    QuestionDao questionDao() {
//        return new CsvQuestionDao(testFileNameProvider());
//    }
//
//    @Bean
//    TestService testService() {
//        return new TestServiceImpl(ioService(), questionDao());
//    }
//
//    @Bean
//    TestRunnerService testRunnerService() {
//        return new TestRunnerServiceImpl(testService(), studentService(), resultService());
//    }
}
