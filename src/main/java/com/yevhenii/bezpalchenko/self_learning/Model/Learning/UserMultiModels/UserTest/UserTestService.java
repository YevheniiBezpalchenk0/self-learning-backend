package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTest;

import com.yevhenii.bezpalchenko.self_learning.DTO.UserAnswerDTO;
import com.yevhenii.bezpalchenko.self_learning.DTO.UserTestDTO;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.Answer;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.AnswerRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.Question;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.QuestionRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserQuestion.UserQuestion;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Test.Test;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Test.TestRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import com.yevhenii.bezpalchenko.self_learning.Model.User.UserRepository;
import com.yevhenii.bezpalchenko.self_learning.config.JwtService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserTestService implements IService<UserTestDTO> {

    private final TestRepository testRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserTestRepository userTestRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    @Override
    public List<UserTestDTO> getAll() {
        return userTestRepository.findAll().stream()
                .map(userTest -> {
                    UserTestDTO userTestDTO = modelMapper.map(userTest, UserTestDTO.class);
                    userTestDTO.setTotalScore(userTest.getScore());
                    return userTestDTO;
                })
                .toList();
    }

    @Override
    public Optional<UserTestDTO> getById(int id) {
        return userTestRepository.findById(id)
                .map(userTest -> {
                    UserTestDTO userTestDTO = modelMapper.map(userTest, UserTestDTO.class);
                    userTestDTO.setTotalScore(userTest.getScore());
                    return userTestDTO;
                });
    }

    @Override
    public UserTestDTO update(int id, UserTestDTO updated) {
        UserTest existingUserTest = userTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserTest not found"));

        existingUserTest.setScore(updated.getTotalScore());
        existingUserTest.getUserQuestions().clear();

        for (UserAnswerDTO answerDTO : updated.getAnswers()) {
            Question question = questionRepository.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            Answer chosenAnswer = answerRepository.findById(answerDTO.getAnswerId())
                    .orElseThrow(() -> new RuntimeException("Answer not found"));

            UserQuestion userQuestion = new UserQuestion();
            userQuestion.setUser(existingUserTest.getUser());
            userQuestion.setQuestion(question);
            userQuestion.setChosenAnswer(chosenAnswer);
            userQuestion.setScore(answerDTO.getScore());

            existingUserTest.getUserQuestions().add(userQuestion);
        }

        UserTest updatedUserTest = userTestRepository.save(existingUserTest);

        UserTestDTO result = modelMapper.map(updatedUserTest, UserTestDTO.class);
        result.setTotalScore(updatedUserTest.getScore());
        return result;
    }

    @Override
    public void delete(int id) {
        if (!userTestRepository.existsById(id)) {
            throw new RuntimeException("UserTest not found");
        }
        userTestRepository.deleteById(id);
    }

    public UserTestDTO evaluateTest(UserTestDTO userTestDTO, String token) {
        int userId = jwtService.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Test test = testRepository.findById(userTestDTO.getId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        UserTest userTest = new UserTest();
        userTest.setUser(user);
        userTest.setTest(test);

        int totalScore = 0;

        for (UserAnswerDTO answerDTO : userTestDTO.getAnswers()) {
            Question question = questionRepository.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            Answer chosenAnswer = answerRepository.findById(answerDTO.getAnswerId())
                    .orElseThrow(() -> new RuntimeException("Answer not found"));

            UserQuestion userQuestion = new UserQuestion();
            userQuestion.setUser(user);
            userQuestion.setQuestion(question);
            userQuestion.setChosenAnswer(chosenAnswer);

            if (question.isCorrect(chosenAnswer)) {
                userQuestion.setScore(question.getPoints());
                answerDTO.setScore(question.getPoints());
                answerDTO.setCorrect(true);
                totalScore += question.getPoints();
            } else {
                userQuestion.setScore(0);
                answerDTO.setScore(0);
                answerDTO.setCorrect(false);
            }

            userTest.getUserQuestions().add(userQuestion);
        }

        userTest.setScore(totalScore);
        userTestRepository.save(userTest);


        UserTestDTO result = new UserTestDTO();
        result.setId(userTest.getTest().getId());
        result.setTotalScore(userTest.getScore());
        result.setAnswers(userTestDTO.getAnswers());
        return result;
    }
}
