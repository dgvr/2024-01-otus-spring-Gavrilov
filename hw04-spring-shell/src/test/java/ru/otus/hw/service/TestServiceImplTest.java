package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
class TestServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    @Test
    void executeTestFor() {
        Student student = new Student("Ivan", "Ivanov");
        int correctAnswerPosition = 2;
        when(ioService.readIntForRangeWithPromptLocalized(
                anyInt(),
                anyInt(),
                anyString(),
                anyString())).thenReturn(correctAnswerPosition);

        Question question = new Question("testQuestion",
                Arrays.asList(
                        new ru.otus.hw.domain.Answer("notCorrectAnswer1", false),
                        new ru.otus.hw.domain.Answer("correctAnswer", true),//correctAnswerPosition
                        new ru.otus.hw.domain.Answer("notCorrectAnswer2", false)));
        when(questionDao.findAll()).then((Answer<List<Question>>) invocation -> {
            List<Question> result = new ArrayList<>();

            result.add(question);
            return result;
        });
        TestResult result = testService.executeTestFor(student);

        TestResult expectedResult = getExpectedResult(student, question);

        assertThat(result).isEqualTo(expectedResult);
    }

    private TestResult getExpectedResult(Student student, Question question) {
        TestResult expectedResult = new TestResult(student);
        expectedResult.setRightAnswersCount(1);
        expectedResult.getAnsweredQuestions().add(question);
        return expectedResult;
    }
}