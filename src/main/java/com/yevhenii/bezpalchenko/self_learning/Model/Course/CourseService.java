package com.yevhenii.bezpalchenko.self_learning.Model.Course;

import com.yevhenii.bezpalchenko.self_learning.Exception.CourseAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(int id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByName(String name) {
        return courseRepository.findByName(name);
    }
    public Course createCourse(Course course) {
        if (courseRepository.findByName(course.getName()).isPresent()) {
            throw new CourseAlreadyExistsException("Course with name " + course.getName() + " already exists");
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(int id, Course updatedCourse) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setName(updatedCourse.getName());
                    course.setDescription(updatedCourse.getDescription());
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

}
