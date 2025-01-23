package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Test;

import com.yevhenii.bezpalchenko.self_learning.DTO.TestDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tests")
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping
    public ResponseEntity<List<TestDTO>> getAllTests() {
        List<TestDTO> tests = testService.getAll();
        if (tests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tests);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TestDTO> getTestById(@PathVariable int id) {
        Optional<TestDTO> test = testService.getById(id);
        return test.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{lessonId}")
    public ResponseEntity<TestDTO> createTest(@RequestBody TestDTO newTest, @PathVariable int lessonId) {
        TestDTO createdTest = testService.create(newTest, lessonId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTest);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TestDTO> updateTest(@PathVariable int id, @RequestBody TestDTO updated) {
        try {
            TestDTO test = testService.update(id, updated);
            return ResponseEntity.ok(test);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable int id) {
        try {
            testService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    }
