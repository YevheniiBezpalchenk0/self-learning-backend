package com.yevhenii.bezpalchenko.self_learning.Model.Theme;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    // Get all themes
    @GetMapping
    public List<Theme> getAllThemes() {
        return themeService.getAllThemes();
    }

    // Get a theme by ID
    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable int id) {
        Optional<Theme> theme = themeService.getThemeById(id);
        return theme.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new theme
    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme newTheme = themeService.saveTheme(theme);
        return ResponseEntity.ok(newTheme);
    }

    // Update an existing theme
    @PutMapping("/{id}")
    public ResponseEntity<Theme> updateTheme(@PathVariable int id, @RequestBody Theme themeDetails) {
        try {
            Theme updatedTheme = themeService.updateTheme(id, themeDetails);
            return ResponseEntity.ok(updatedTheme);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a theme by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable int id) {
        try {
            themeService.deleteTheme(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Mark a theme as completed
    @PutMapping("/{id}/complete")
    public ResponseEntity<Theme> markThemeCompleted(@PathVariable int id) {
        try {
            Theme completedTheme = themeService.markThemeCompleted(id);
            return ResponseEntity.ok(completedTheme);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}