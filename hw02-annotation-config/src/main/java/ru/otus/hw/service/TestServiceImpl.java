package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

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

        for (int i = 1; i <= questions.size(); i++) {
            Question question = questions.get(i - 1);
            printQuestion(i, question);
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

    private int processAnswers(List<Answer> answers) {
        int correctAnswer = 0;
        for (int i = 1; i <= answers.size(); i++) {
            String aCounter = i + ") ";
            Answer answer = answers.get(i - 1);
            ioService.printLine(aCounter + answer.text());
            if (answer.isCorrect()) {
                correctAnswer = i;
            }
        }
        return correctAnswer;
    }

    private int getUserAnswer(int answersCount) {
        return ioService.readIntForRangeWithPrompt(0,
                answersCount,
                "Choose the correct answer",
                "There is no such option! Choose the correct answer again");
    }
}
