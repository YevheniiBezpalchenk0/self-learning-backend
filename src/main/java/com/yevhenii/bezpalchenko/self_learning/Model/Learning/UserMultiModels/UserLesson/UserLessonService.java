package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserLesson;

import com.yevhenii.bezpalchenko.self_learning.DTO.UserLessonDTO;
import com.yevhenii.bezpalchenko.self_learning.DTO.UserTopicDTO;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson.Lesson;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson.LessonRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTopic.UserTopic;
import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import com.yevhenii.bezpalchenko.self_learning.Model.User.UserRepository;
import com.yevhenii.bezpalchenko.self_learning.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLessonService implements IService<UserLessonDTO> {

    private final JwtService jwtService;
    private final UserLessonRepository userLessonRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserLessonDTO> getAll() {
        return userLessonRepository.findAll().stream()
                .map(userLesson -> modelMapper.map(userLesson, UserLessonDTO.class))
                .collect(Collectors.toList());
    }

    public UserLessonDTO create(Lesson lessonDTO, UserTopic userTopic, String token) {
        int userId = jwtService.extractUserId(token);
        User student = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lesson lesson = lessonRepository.findById(lessonDTO.getId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        UserLesson userLesson = new UserLesson();
        userLesson.setUser(student);
        userLesson.setLesson(lesson);
        userLesson.setUserTopic(userTopic);
        userLesson.setCompleted(false);
        userLesson.setGrade(0);

        UserLesson savedUserLesson = userLessonRepository.save(userLesson);
        return modelMapper.map(savedUserLesson, UserLessonDTO.class);
    }

    @Override
    public Optional<UserLessonDTO> getById(int id) {
        return userLessonRepository.findById(id)
                .map(userLesson -> modelMapper.map(userLesson, UserLessonDTO.class));
    }

    public UserLessonDTO completedLesson(int id) {
        UserLesson userLesson = userLessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserLesson not found"));
        userLesson.setCompleted(true);
        UserLesson updatedUserLesson = userLessonRepository.save(userLesson);
        return modelMapper.map(updatedUserLesson, UserLessonDTO.class);
    }
    @Override
    public UserLessonDTO update(int id, UserLessonDTO updated) {
        UserLesson userLesson = userLessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserLesson not found"));

        userLesson.setGrade(updated.getGrade());
        userLesson.setCompleted(updated.isCompleted());
        UserLesson updatedUserLesson = userLessonRepository.save(userLesson);
        return modelMapper.map(updatedUserLesson, UserLessonDTO.class);
    }
    @Override
    public void delete(int id) {
        userLessonRepository.deleteById(id);
    }

    public void  createUserLesson(UserLessonDTO userLessonDTO, UserTopicDTO topicDTO) {
        UserLesson userLesson = modelMapper.map(userLessonDTO, UserLesson.class);
        UserTopic topic = modelMapper.map(topicDTO, UserTopic.class);
        userLesson.setUserTopic(topic);
        userLessonRepository.save(userLesson);
    }
}
