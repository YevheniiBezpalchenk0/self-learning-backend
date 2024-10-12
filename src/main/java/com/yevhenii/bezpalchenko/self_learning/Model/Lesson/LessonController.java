package com.yevhenii.bezpalchenko.self_learning.Model.Lesson;

import com.yevhenii.bezpalchenko.self_learning.Model.Theme.Theme;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;


    // Get all lessons
    @GetMapping
    public List<Lesson> getAllLessons() {
        return lessonService.getAllLessons();
    }

    // Get lesson by ID
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable int id) {
        Optional<Lesson> lesson = lessonService.getLessonById(id);
        return lesson.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get lessons by theme ID
    @GetMapping("/theme/{themeId}")
    public List<Lesson> getLessonsByTheme(@PathVariable int themeId) {
        Theme theme = new Theme(); // Assuming you have a Theme class that matches the themeId
        theme.setId(themeId);
        return lessonService.getLessonsByTheme(theme);
    }

    // Add a new lesson
    @PostMapping
    public ResponseEntity<Lesson> addLesson(@RequestBody Lesson lesson) {
        Lesson newLesson = lessonService.addLesson(lesson);
        return ResponseEntity.ok(newLesson);
    }

    // Update a lesson by ID
    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable int id, @RequestBody Lesson lessonDetails) {
        Lesson updatedLesson = lessonService.updateLesson(id, lessonDetails);
        return ResponseEntity.ok(updatedLesson);
    }

    // Delete a lesson by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable int id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    // Mark lesson as completed
    @PutMapping("/{id}/complete")
    public ResponseEntity<Lesson> markLessonCompleted(@PathVariable int id) {
        Lesson completedLesson = lessonService.markLessonCompleted(id);
        return ResponseEntity.ok(completedLesson);
    }
}
