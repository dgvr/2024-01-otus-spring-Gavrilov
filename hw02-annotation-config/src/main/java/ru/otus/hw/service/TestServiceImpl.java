package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        int questionsCounter = 0;
        for (var question : questions) {
            printQuestion(++questionsCounter, question);
            int correctAnswer = processAnswers(question.answers());
            int userAnswer = getUserAnswer(question.answers().size());
            var isAnswerValid = correctAnswer == userAnswer;
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printQuestion(int counter, Question question) {
        ioService.printFormattedLine(counter + ". " + question.text() + "%n");
    }

    private int processAnswers(Collection<Answer> answers) {
        int correctAnswer = 0;
        int answerCounter = 0;
        for (Answer answer : answers) {
            String aCounter = ++answerCounter + ") ";
            ioService.printLine(aCounter + answer.text());
            if (answer.isCorrect()) {
                correctAnswer = answerCounter;
            }
        }
        return correctAnswer;
    }

    private int getUserAnswer(int answersCount) {
        int result = -1;
        String userAnswer = ioService.readStringWithPrompt("Choose the correct answer");
        while (result == -1) {
            try {
                int tempResult = Integer.parseInt(userAnswer);
                if (!isCorrectAnswerPosition(tempResult, 0, answersCount)) {
                    userAnswer = ioService.readStringWithPrompt(
                            "There is no such option! Choose the correct answer again");
                    continue;
                }
                result = tempResult;
            } catch (NumberFormatException e) {
                userAnswer = ioService.readStringWithPrompt("Incorrect attempt! Choose the correct answer again");
            }
        }
        return result;
    }

    private boolean isCorrectAnswerPosition(int answer, int minBorder, int maxBorder) {
        return answer >= minBorder && answer <= maxBorder;
    }
}
