package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName())) {
            if (is == null) {
                return new ArrayList<>();
            }
            List<QuestionDto> questionDtoList = new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(is))
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build().parse();

            List<Question> result = new ArrayList<>(questionDtoList.size());
            questionDtoList.forEach(dto -> result.add(dto.toDomainObject()));
            return result;

        } catch (IOException e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }
}
