package com.yevhenii.bezpalchenko.self_learning.Model.Lesson;

import com.yevhenii.bezpalchenko.self_learning.Model.Theme.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    // Retrieve all lessons
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    // Retrieve a lesson by its ID
    public Optional<Lesson> getLessonById(int id) {
        return lessonRepository.findById(id);
    }

    // Retrieve lessons by theme
    public List<Lesson> getLessonsByTheme(Theme theme) {
        return lessonRepository.findByTheme(theme);
    }

    // Add a new lesson
    public Lesson addLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    // Update an existing lesson
    public Lesson updateLesson(int id, Lesson lessonDetails) {
        return lessonRepository.findById(id).map(lesson -> {
            lesson.setName(lessonDetails.getName());
            lesson.setCompleted(lessonDetails.isCompleted());
            lesson.setGrade(lessonDetails.getGrade());
            return lessonRepository.save(lesson);
        }).orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    // Delete a lesson by ID
    public void deleteLesson(int id) {
        lessonRepository.deleteById(id);
    }

    // Mark a lesson as completed
    public Lesson markLessonCompleted(int id) {
        return lessonRepository.findById(id).map(lesson -> {
            lesson.setCompleted();
            return lessonRepository.save(lesson);
        }).orElseThrow(() -> new RuntimeException("Lesson not found"));
    }
}
