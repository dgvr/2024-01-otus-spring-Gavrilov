package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = AppConfig.class)
@ExtendWith(SpringExtension.class)
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao questionDao;

    @Test
    void findAll() {
        List<Question> questionList = questionDao.findAll();
        List<Question> expectedQuestionList = generateExpectedQuestionList();
        assertThat(questionList).isEqualTo(expectedQuestionList);
    }


    private List<Question> generateExpectedQuestionList() {

        Question question1 = new Question("Is there life on Mars?",
                Arrays.asList(
                        new Answer("Science doesn't know this yet", true),
                        new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                        new Answer("Absolutely not", false))
        );

        Question question2 = new Question("How should resources be loaded form jar in Java?",
                Arrays.asList(
                        new Answer("ClassLoader#geResourceAsStream or ClassPathResource#getInputStream", true),
                        new Answer("ClassLoader#geResource#getFile + FileReader", false),
                        new Answer("Wingardium Leviosa", false))
        );

        Question question3 = new Question("Which option is a good way to handle the exception?",
                Arrays.asList(
                        new Answer("@SneakyThrow", false),
                        new Answer("e.printStackTrace()", false),
                        new Answer("Rethrow with wrapping in business exception (for example, QuestionReadException)", true),
                        new Answer("Ignoring exception", false))
        );

        Question question4 = new Question("Was this homework useful?",
                Arrays.asList(
                        new Answer("Yes", true),
                        new Answer("No", false))
        );

        Question question5 = new Question("Which bird flies away for the winter?",
                Arrays.asList(
                        new Answer("Seagull", false),
                        new Answer("Rooks", true),
                        new Answer("Pigeon", false))
        );

        List<Question> expectedQuestionList = new ArrayList<>();
        expectedQuestionList.add(question1);
        expectedQuestionList.add(question2);
        expectedQuestionList.add(question3);
        expectedQuestionList.add(question4);
        expectedQuestionList.add(question5);

        return expectedQuestionList;
    }
}