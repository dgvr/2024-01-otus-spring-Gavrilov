package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvQuestionDaoTest {

    @Test
    void findAll() {
        List<Question> questionList = new CsvQuestionDao(new AppProperties("notExistentFile")).findAll();
        assertEquals(questionList, Collections.EMPTY_LIST);

        questionList = new CsvQuestionDao(new AppProperties("testQuestions.csv")).findAll();
        assertEquals(questionList.size(), 5);
        assertEquals(3, questionList.get(0).answers().size());
        assertEquals(3, questionList.get(1).answers().size());
        assertEquals(4, questionList.get(2).answers().size());
        assertEquals(2, questionList.get(3).answers().size());
        assertEquals(3, questionList.get(4).answers().size());
    }
}