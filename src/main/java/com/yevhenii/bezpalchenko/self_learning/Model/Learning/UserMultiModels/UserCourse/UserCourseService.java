package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserCourse;

import com.yevhenii.bezpalchenko.self_learning.DTO.*;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course.Course;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course.CourseRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson.Lesson;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserLesson.UserLessonService;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Topic.Topic;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTopic.UserTopicService;
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
public class UserCourseService implements IService<UserCourseDTO> {

    private final JwtService jwtService;
    private final UserCourseRepository userCourseRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserLessonService userLessonService;
    private final ModelMapper modelMapper;
    private final UserTopicService userTopicService;
    @Override
    public List<UserCourseDTO> getAll() {
        return userCourseRepository.findAll().stream()
                .map(userCourse -> modelMapper.map(userCourse, UserCourseDTO.class))
                .collect(Collectors.toList());
    }
    public UserCourseDTO create(CourseDTO courseDTO, String token){
        int userId = jwtService.extractUserId(token);
        User student = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Получаем курс
        Course course = courseRepository.findById(courseDTO.getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        UserCourse userCourse = new UserCourse();
        userCourse.setStudent(student);
        userCourse.setCourse(course);
        userCourse.setGrade(0);
        userCourseRepository.save(userCourse);
        for (Topic topic : course.getTopics()) {
            UserTopicDTO userTopicDTO = new UserTopicDTO();
            userTopicDTO.setId(topic.getId());
            userTopicDTO.setUser(modelMapper.map(student, UserDTO.class));
            userTopicDTO.setTopic(modelMapper.map(topic, TopicDTO.class));
            userTopicDTO.setProgress(0);

            userTopicService.createUserTopic(userTopicDTO, userCourse);

            for (Lesson lesson : topic.getLessons()) {
                UserLessonDTO userLessonDTO = new UserLessonDTO();
                userLessonDTO.setLesson(modelMapper.map(lesson, LessonDTO.class));
                userLessonDTO.setUser(modelMapper.map(student, UserDTO.class));
                userLessonDTO.setGrade(0);
                userLessonDTO.setCompleted(false);

                userLessonService.createUserLesson(userLessonDTO, userTopicDTO);
            }
        }
        UserCourse createdUserCourse = userCourseRepository.findById(userCourse.getId())
                .orElseThrow(() -> new RuntimeException("UserCourse not found"));

        return modelMapper.map(createdUserCourse, UserCourseDTO.class);
    }
    @Override
    public Optional<UserCourseDTO> getById(int id) {
        return userCourseRepository.findById(id)
                .map(userCourse -> modelMapper.map(userCourse, UserCourseDTO.class));
    }

    @Override
    public UserCourseDTO update(int id, UserCourseDTO updated) {
        UserCourse userCourse = userCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserCourse not found"));

        userCourse.setGrade(updated.getGrade());

        UserCourse updatedUserCourse = userCourseRepository.save(userCourse);
        return modelMapper.map(updatedUserCourse, UserCourseDTO.class);
    }

    @Override
    public void delete(int id) {
        userCourseRepository.deleteById(id);
    }

}
