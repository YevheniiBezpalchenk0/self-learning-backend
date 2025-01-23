package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Topic;

import com.yevhenii.bezpalchenko.self_learning.DTO.TopicDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/topics")
@Slf4j
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicDTO>> getTopics() {
        List<TopicDTO> topics = topicService.getAll();
        if (topics.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable int id) {
        Optional<TopicDTO> topic = topicService.getById(id);
        return topic.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<TopicDTO> createTopic(@RequestBody Topic newTopic, @PathVariable int courseId) {
        log.info("topic:" + newTopic);
        TopicDTO createdTopic = topicService.create(newTopic, courseId);
        System.out.println("Topic:" + createdTopic);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDTO> updateTopic(@PathVariable int id, @RequestBody TopicDTO updated) {
        try {
            TopicDTO topic = topicService.update(id, updated);
            return ResponseEntity.ok(topic);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable int id) {
        try {
            topicService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
