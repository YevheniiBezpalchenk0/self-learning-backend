package com.yevhenii.bezpalchenko.self_learning.Model.Theme;

import com.yevhenii.bezpalchenko.self_learning.Model.Course.Course;
import com.yevhenii.bezpalchenko.self_learning.Model.Lesson.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    // Get all themes
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    // Get theme by ID
    public Optional<Theme> getThemeById(int id) {
        return themeRepository.findById(id);
    }

    // Get all themes for a course
    public List<Theme> getThemesByCourse(Course course) {
        return themeRepository.findByCourse(course);  // You may need to add this method in the ThemeRepository
    }

    // Add or save a new theme
    public Theme saveTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    // Update an existing theme
    public Theme updateTheme(int id, Theme themeDetails) {
        return themeRepository.findById(id).map(theme -> {
            theme.setName(themeDetails.getName());
            theme.setGrade(themeDetails.getGrade());
            theme.setCompleted(themeDetails.isCompleted());
            theme.setCourse(themeDetails.getCourse());
            theme.setLessons(themeDetails.getLessons());
            return themeRepository.save(theme);
        }).orElseThrow(() -> new RuntimeException("Theme not found with id " + id));
    }

    // Delete a theme by ID
    public void deleteTheme(int id) {
        themeRepository.deleteById(id);
    }

    // Mark a theme as completed
    public Theme markThemeCompleted(int id) {
        return themeRepository.findById(id).map(theme -> {
            theme.checkCompleted();
            return themeRepository.save(theme);
        }).orElseThrow(() -> new RuntimeException("Theme not found with id " + id));
    }

    // Calculate the grade for a theme based on its lessons
    public void calculateGrade(Theme theme) {
        double gradeVar = 0;
        for (Lesson lesson : theme.getLessons()) {
            gradeVar += lesson.getGrade();
        }
        theme.setGrade(gradeVar);
        themeRepository.save(theme);
    }
}
