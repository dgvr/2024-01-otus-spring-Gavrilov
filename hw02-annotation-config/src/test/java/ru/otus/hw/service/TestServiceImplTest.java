package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    IOService ioService;

    @Mock
    QuestionDao questionDao;

    @Test
    void executeTestFor() {
        Student student = new Student("Ivan", "Ivanov");
        String correctAnswerPosition = "2";
        when(ioService.readStringWithPrompt(anyString())).thenReturn(correctAnswerPosition);
        when(questionDao.findAll()).then((Answer<List<Question>>) invocation -> {
            List<Question> result = new ArrayList<>();
            Question question = new Question("testQuestion",
                    Arrays.asList(
                            new ru.otus.hw.domain.Answer("notCorrectAnswer1", false),
                            new ru.otus.hw.domain.Answer("correctAnswer", true),//correctAnswerPosition
                            new ru.otus.hw.domain.Answer("notCorrectAnswer2", false)));
            result.add(question);
            return result;
        });
        TestService testService = new TestServiceImpl(ioService, questionDao);
        TestResult result = testService.executeTestFor(student);

        assertEquals("Ivan", result.getStudent().firstName());
        assertEquals("Ivanov", result.getStudent().lastName());
        assertEquals(1, result.getAnsweredQuestions().size());
        assertEquals(1, result.getRightAnswersCount());


    }
}