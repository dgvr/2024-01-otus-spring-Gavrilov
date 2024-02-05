package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();
        int[] questionsCounter = new int[1];
        questions.forEach(question -> {
            String qCounter = ++questionsCounter[0] + ". ";
            ioService.printFormattedLine(qCounter + question.text() + "%n");
            List<Answer> answers = question.answers();
            if (answers != null) {
                int[] answerCounter = new int[1];
                answers.forEach(answer -> {
                    String aCounter = ++answerCounter[0] + ") ";
                    ioService.printLine(aCounter + answer.text());
                });
            }
            ioService.printFormattedLine("%n");
        });
    }
}
