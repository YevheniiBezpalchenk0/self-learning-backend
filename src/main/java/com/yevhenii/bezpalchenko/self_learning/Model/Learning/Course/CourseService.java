package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course;

import com.yevhenii.bezpalchenko.self_learning.DTO.CourseDTO;
import com.yevhenii.bezpalchenko.self_learning.Exception.EntityAlreadyExistsException;
import com.yevhenii.bezpalchenko.self_learning.Exception.UnauthorizedException;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import com.yevhenii.bezpalchenko.self_learning.Model.Token.Token;
import com.yevhenii.bezpalchenko.self_learning.Model.Token.TokenRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import com.yevhenii.bezpalchenko.self_learning.Model.User.UserRepository;
import com.yevhenii.bezpalchenko.self_learning.config.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CourseService implements IService<CourseDTO> {
    private final CourseRepository courseRepository;
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public List<CourseDTO> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CourseDTO> getById(int id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + id + " not found"));
        return Optional.of(modelMapper.map(course, CourseDTO.class));
    }

    public CourseDTO create(CourseDTO newCourseDTO, String stoken) {
        Optional<Course> existingCourse = courseRepository.findByTitle(newCourseDTO.getTitle());
        if (existingCourse.isPresent()) {
            throw new EntityAlreadyExistsException("Course with that name already presents: " + newCourseDTO.getTitle());
        }

        Course newCourse = modelMapper.map(newCourseDTO, Course.class);

        Optional<Token> token = tokenRepository.findByToken(stoken);
        if (token.isPresent()) {
            User user = token.get().getUser();
            newCourse.setCreator(user);
            Course savedCourse = courseRepository.save(newCourse);
            return modelMapper.map(savedCourse, CourseDTO.class);
        } else {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }

    @Override
    public CourseDTO update(int id, CourseDTO updatedDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + id + " not found"));

        if (updatedDTO.getTitle() != null) {
            existingCourse.setTitle(updatedDTO.getTitle());
        }
        if (updatedDTO.getDescription() != null) {
            existingCourse.setDescription(updatedDTO.getDescription());
        }

        Course updatedCourse = courseRepository.save(existingCourse);
        return modelMapper.map(updatedCourse, CourseDTO.class);
    }

    @Override
    public void delete(int id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + id + " not found"));
        courseRepository.delete(course);
    }

    public Optional<CourseDTO> getByTitle(String title) {
        Course course = courseRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Course with title " + title + " not found"));
        return Optional.of(modelMapper.map(course, CourseDTO.class));
    }

    public List<CourseDTO> findByToken(String token) {
        int userId = jwtService.extractUserId(token);
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with Id " + userId + " not found"));
        return courseRepository.findAllByCreator(creator)
                .stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }
}