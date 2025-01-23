package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question;

import com.yevhenii.bezpalchenko.self_learning.DTO.AnswerDTO;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnswerService implements IService<AnswerDTO> {
    private final ModelMapper modelMapper;
    private final AnswerRepository answerRepository;

    @Override
    public List<AnswerDTO> getAll() {
        return answerRepository.findAll()
                .stream()
                .map(answer -> modelMapper.map(answer, AnswerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AnswerDTO> getById(int id) {
        return answerRepository.findById(id)
                .map(answer -> modelMapper.map(answer, AnswerDTO.class));
    }
    public Answer create(AnswerDTO answerDTO){
        Answer answer = modelMapper.map(answerDTO, Answer.class);
        return answerRepository.save(answer);
    }
    @Override
    public AnswerDTO update(int id, AnswerDTO updated) {
        Optional<Answer> existingAnswer = answerRepository.findById(id);
        if (existingAnswer.isPresent()) {
            Answer answer = existingAnswer.get();
            answer.setText(updated.getText());
            answer.setCorrect(updated.isCorrect());
            Answer savedAnswer = answerRepository.save(answer);
            return modelMapper.map(savedAnswer, AnswerDTO.class);
        } else {
            throw new IllegalArgumentException("Answer with ID " + id + " not found.");
        }
    }

    @Override
    public void delete(int id) {
        if (answerRepository.existsById(id)) {
            answerRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Answer with ID " + id + " not found.");
        }
    }
}