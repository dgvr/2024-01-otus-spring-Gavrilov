package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.Collection;
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
        printQuestions(questions);
    }

    private void printQuestions(Collection<Question> questions) {
        int questionsCounter = 0;
        for (Question question : questions) {
            printQuestion(++questionsCounter, question);
            printAnswers(question.answers());
            ioService.printFormattedLine("%n");
        }
    }

    private void printQuestion(int counter, Question question) {
        ioService.printFormattedLine(counter + ". " + question.text() + "%n");
    }

    private void printAnswers(Collection<Answer> answers) {
        int answerCounter = 0;
        for (Answer answer : answers) {
            String aCounter = ++answerCounter + ") ";
            ioService.printLine(aCounter + answer.text());
        }
    }
}
