package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Test;

import com.yevhenii.bezpalchenko.self_learning.DTO.QuestionDTO;
import com.yevhenii.bezpalchenko.self_learning.DTO.TestDTO;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson.Lesson;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson.LessonRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.Question;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.QuestionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TestService implements IService<TestDTO> {

    private final TestRepository testRepository;
    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final QuestionService questionService;

    @Override
    public List<TestDTO> getAll() {
        return testRepository.findAll()
                .stream()
                .map(test -> {
                    TestDTO testDTO = modelMapper.map(test, TestDTO.class);
                    testDTO.setQuestions(
                            test.getQuestions().stream()
                                    .map(question -> modelMapper.map(question, QuestionDTO.class))
                                    .collect(Collectors.toList())
                    );
                    return testDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TestDTO> getById(int id) {
        return testRepository.findById(id)
                .map(test -> {
                    TestDTO testDTO = modelMapper.map(test, TestDTO.class);
                    testDTO.setQuestions(
                            test.getQuestions().stream()
                                    .map(question -> modelMapper.map(question, QuestionDTO.class))
                                    .collect(Collectors.toList())
                    );
                    return testDTO;
                });
    }

    @Override
    public TestDTO update(int id, TestDTO updated) {
        Optional<Test> existingTest = testRepository.findById(id);
        if (existingTest.isPresent()) {
            Test test = existingTest.get();
            test.setTitle(updated.getTitle());
            test.getQuestions().clear();
            test.getQuestions().addAll(
                    updated.getQuestions().stream()
                            .map(questionDTO -> modelMapper.map(questionDTO, Question.class))
                            .toList()
            );
            Test savedTest = testRepository.save(test);
            return modelMapper.map(savedTest, TestDTO.class);
        } else {
            throw new IllegalArgumentException("Тест с ID " + id + " не найден.");
        }
    }

    @Override
    public void delete(int id) {
        if (testRepository.existsById(id)) {
            testRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Тест с ID " + id + " не найден.");
        }
    }

    public TestDTO create(TestDTO newTest, int lessonId) {
       Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson with ID " + lessonId + " not found."));

        Test test = new Test();
        test.setTitle(newTest.getTitle());
        test.setLesson(lesson);

        List<Question> questions = questionService.createFromList(newTest.getQuestions());
        test.setQuestions(questions);

        Test savedTest = testRepository.save(test);
        Test result = testRepository.findById(savedTest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Test with ID " + savedTest.getId() + " not found."));

        return modelMapper.map(result, TestDTO.class);
    }
}