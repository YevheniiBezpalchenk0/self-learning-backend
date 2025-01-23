package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course;

import com.yevhenii.bezpalchenko.self_learning.DTO.CourseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getCourses() {
        List<CourseDTO> courses = service.getAll();
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable int id) {
        Optional<CourseDTO> course = service.getById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    public ResponseEntity<List<CourseDTO>> getCoursesOfUser(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Возвращаем 401, если токен не передан
        }

        String token = authHeader.substring(7);
        List<CourseDTO> courses = service.findByToken(token);
        return ResponseEntity.ok(courses);

    }

    @GetMapping("/title/{title}")
    public ResponseEntity<CourseDTO> getCourseByTitle(@PathVariable String title) {
        Optional<CourseDTO> course = service.getByTitle(title);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO newCourse, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Возвращаем 401, если токен не передан
        }
        String token = authHeader.substring(7);
        CourseDTO createdCourse = service.create(newCourse, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable int id, @RequestBody CourseDTO updated) {
        try {
            CourseDTO course = service.update(id, updated);
            return ResponseEntity.ok(course); // Возвращаем обновленный курс с статусом 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Если курс не найден, возвращаем 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Для всех других ошибок возвращаем 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
