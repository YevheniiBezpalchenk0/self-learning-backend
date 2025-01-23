package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question;

import com.yevhenii.bezpalchenko.self_learning.DTO.AnswerDTO;
import com.yevhenii.bezpalchenko.self_learning.DTO.QuestionDTO;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionService implements IService<QuestionDTO> {
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;

    @Override
    public List<QuestionDTO> getAll() {
        return questionRepository.findAll()
                .stream()
                .map(question -> modelMapper.map(question, QuestionDTO.class))
                .collect(Collectors.toList());
    }

    // Получение вопроса по ID
    @Override
    public Optional<QuestionDTO> getById(int id) {
        return questionRepository.findById(id)
                .map(question -> modelMapper.map(question, QuestionDTO.class));
    }

    @Override
    public QuestionDTO update(int id, QuestionDTO updated) {
        Optional<Question> existingQuestion = questionRepository.findById(id);
        if (existingQuestion.isPresent()) {
            Question question = existingQuestion.get();
            question.setText(updated.getText());
            question.setAnswers(modelMapper.map(updated, Question.class).getAnswers());
            question.setPoints(updated.getPoints());
            Question savedQuestion = questionRepository.save(question);
            return modelMapper.map(savedQuestion, QuestionDTO.class);
        } else {
            throw new IllegalArgumentException("Question with ID " + id + " not found.");
        }
    }
    public List<Question> createFromList(List<QuestionDTO> questions){
        List<Question> questionList = new ArrayList<>();
        for(QuestionDTO questionDTO : questions) {
            Question question = new Question();
            question.setText(questionDTO.getText());
            List<Answer> answersList = new ArrayList<>();
            for (AnswerDTO answerDTO: questionDTO.getAnswers()){
                Answer answer = answerService.create(answerDTO);
                answersList.add(answer);
            }
            question.setAnswers(answersList);
            question.setPoints(questionDTO.getPoints());
            questionRepository.save(question);
            questionList.add(question);
        }
        return questionList;
    }
    @Override
    public void delete(int id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Question with ID " + id + " not found.");
        }
    }
}
