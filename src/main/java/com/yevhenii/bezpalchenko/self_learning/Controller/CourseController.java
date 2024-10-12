package com.yevhenii.bezpalchenko.self_learning.Controller;
import lombok.extern.slf4j.Slf4j;
import com.yevhenii.bezpalchenko.self_learning.Model.Course.Course;
import com.yevhenii.bezpalchenko.self_learning.Model.Course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/courses")
@Slf4j
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody Course course) {
        Course createdCourse = service.createCourse(course);
        log.info("Create Course:" + course);
        return ResponseEntity.ok(createdCourse);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = service.getAllCourses();
        log.info("Get All Courses:" + courses);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        Optional<Course> course = service.getCourseById(id);
        log.info("Get Course:" + course);
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Course> getCourseByName(@PathVariable String name) {
        Optional<Course> course = service.getCourseByName(name);
        log.info("Get Course By Name:" + course);
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable int id,
            @RequestBody Course updatedCourse
    ) {

        Course course = service.updateCourse(id, updatedCourse);
        log.info("Update Course:" + course);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        service.deleteCourse(id);
        log.info("Deleted Course:" + id);
        return ResponseEntity.noContent().build();
    }
}
