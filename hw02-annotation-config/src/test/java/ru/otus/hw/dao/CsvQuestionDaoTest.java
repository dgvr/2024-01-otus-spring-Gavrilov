package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.AppConfig;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = AppConfig.class)
@ExtendWith(SpringExtension.class)
class CsvQuestionDaoTest {

    @Autowired
    CsvQuestionDao questionDao;

    @Test
    void findAll() {
        List<Question> questionList = questionDao.findAll();

        assertEquals(questionList.size(), 5);
        assertEquals(3, questionList.get(0).answers().size());
        assertEquals(3, questionList.get(1).answers().size());
        assertEquals(4, questionList.get(2).answers().size());
        assertEquals(2, questionList.get(3).answers().size());
        assertEquals(3, questionList.get(4).answers().size());
    }
}