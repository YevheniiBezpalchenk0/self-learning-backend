package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserCourse;

import com.yevhenii.bezpalchenko.self_learning.DTO.CourseDTO;
import com.yevhenii.bezpalchenko.self_learning.DTO.UserCourseDTO;
import com.yevhenii.bezpalchenko.self_learning.Exception.EntityAlreadyExistsException;
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
@RequestMapping("/api/userCourses")
@Slf4j
@RequiredArgsConstructor
public class UserCourseController {

    private final UserCourseService service;

    @GetMapping
    public ResponseEntity<List<UserCourseDTO>> getUserCourses() {
        List<UserCourseDTO> userCourses = service.getAll();
        if (userCourses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userCourses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCourseDTO> getUserCourseById(@PathVariable int id) {
        Optional<UserCourseDTO> userCourse = service.getById(id);
        return userCourse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserCourseDTO> createUserCourse(@RequestBody CourseDTO courseDTO, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Возвращаем 401, если токен не передан
            }

            // Извлекаем токен из заголовка
            String token = authHeader.substring(7);
            UserCourseDTO createdUserCourse = service.create(courseDTO, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserCourse);
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Если связь уже существует
        } catch (Exception e) {
            log.error("Ошибка при создании UserCourse", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCourseDTO> updateUserCourse(@PathVariable int id, @RequestBody UserCourseDTO updated) {
        try {
            UserCourseDTO userCourse = service.update(id, updated);
            return ResponseEntity.ok(userCourse); // Возвращаем обновленный объект с статусом 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Если объект не найден, возвращаем 404 Not Found
        } catch (Exception e) {
            log.error("Ошибка при обновлении UserCourse", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserCourse(@PathVariable int id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build(); // Успешное удаление — 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Если объект не найден — 404 Not Found
        } catch (Exception e) {
            log.error("Ошибка при удалении UserCourse", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
