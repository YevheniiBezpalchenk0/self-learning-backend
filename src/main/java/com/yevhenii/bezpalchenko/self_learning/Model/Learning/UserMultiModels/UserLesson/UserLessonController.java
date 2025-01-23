package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserLesson;

import com.yevhenii.bezpalchenko.self_learning.DTO.UserLessonDTO;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson.Lesson;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTopic.UserTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userLesson")
@Slf4j
@RequiredArgsConstructor
public class UserLessonController {
    private final UserLessonService userLessonService;

    @GetMapping
    public ResponseEntity<List<UserLessonDTO>> getAllUserLessons() {
        List<UserLessonDTO> userLessons = userLessonService.getAll();
        return ResponseEntity.ok(userLessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserLessonDTO> getUserLessonById(@PathVariable int id) {
        return userLessonService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserLessonDTO> createUserLesson(
            @RequestBody Lesson lessonDTO,
            @RequestBody UserTopic userTopic,
            @RequestHeader("Authorization") String token
    ) {
        log.info("Создание нового UserLesson для Lesson: {}, UserTopic: {}", lessonDTO, userTopic);
        try {

            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            UserLessonDTO createdUserLesson = userLessonService.create(lessonDTO, userTopic, jwtToken);
            return ResponseEntity.ok(createdUserLesson);
        } catch (RuntimeException e) {
            log.error("Ошибка при создании UserLesson: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserLessonDTO> updateUserLesson(
            @PathVariable int id,
            @RequestBody UserLessonDTO updatedUserLessonDTO
    ) {
        log.info("Обновление UserLesson с ID {}: {}", id, updatedUserLessonDTO);
        try {
            UserLessonDTO updated = userLessonService.update(id, updatedUserLessonDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Ошибка при обновлении UserLesson: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserLesson(@PathVariable int id) {
        log.info("Удаление UserLesson с ID {}", id);
        try {
            userLessonService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Ошибка при удалении UserLesson: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/complete/{id}")
    public ResponseEntity<UserLessonDTO> completeLesson(@PathVariable int id) {
        log.info("Завершение урока с ID {}", id);
        try {
            UserLessonDTO completedLesson = userLessonService.completedLesson(id);
            return ResponseEntity.ok(completedLesson);
        } catch (RuntimeException e) {
            log.error("Ошибка при завершении урока: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
