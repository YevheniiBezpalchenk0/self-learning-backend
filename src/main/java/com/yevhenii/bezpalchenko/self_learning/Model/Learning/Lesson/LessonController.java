package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson;

import com.yevhenii.bezpalchenko.self_learning.DTO.LessonDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lessons")
@Slf4j
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getLessons() {
        List<LessonDTO> lessons = lessonService.getAll();
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable int id) {
        Optional<LessonDTO> lesson = lessonService.getById(id);
        return lesson.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/{topicId}")
    public ResponseEntity<LessonDTO> createLesson(@RequestBody Lesson newLesson, @PathVariable int topicId) {
        log.info("Lesson:" + newLesson);
        LessonDTO createdLesson = lessonService.create(newLesson, topicId);
        System.out.println("Lesson:" + createdLesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonDTO> updateLesson(@RequestBody LessonDTO updatedLesson, @PathVariable int lessonId) {
        log.info("request: " + updatedLesson);
        LessonDTO lesson = lessonService.update(lessonId, updatedLesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
    }
}
