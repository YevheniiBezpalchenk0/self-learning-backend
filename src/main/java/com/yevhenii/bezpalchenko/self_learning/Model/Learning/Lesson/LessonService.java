package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson;

import com.yevhenii.bezpalchenko.self_learning.DTO.LessonDTO;
import com.yevhenii.bezpalchenko.self_learning.DTO.TopicDTO;
import com.yevhenii.bezpalchenko.self_learning.Exception.EntityAlreadyExistsException;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Topic.Topic;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Topic.TopicService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonService implements IService<LessonDTO> {

    private final LessonRepository lessonRepository;
    private final TopicService topicService;
    private final ModelMapper modelMapper;

    @Override
    public List<LessonDTO> getAll() {
        return lessonRepository.findAll().stream().map(lesson -> modelMapper.map(lesson, LessonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<LessonDTO> getById(int id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson with id " + id + " not found"));
        return Optional.of(modelMapper.map(lesson, LessonDTO.class));
    }

    public LessonDTO create(Lesson lesson, int topicId) {
        Optional<Lesson> existingLesson = lessonRepository.findByTitle(lesson.getTitle());
        if (existingLesson.isPresent()) {
            throw new EntityAlreadyExistsException("Lesson with that name already presents: " + lesson.getTitle());
        }

        Optional<TopicDTO> topicDTO = topicService.getById(topicId);
        if (topicDTO.isPresent()) {
            Topic topic = modelMapper.map(topicDTO.get(), Topic.class);
            lesson.setTopic(topic);
            Lesson newLesson = lessonRepository.save(lesson);
            return modelMapper.map(newLesson, LessonDTO.class);
        } else {
            throw new EntityNotFoundException("A topic with id" + topicId + " does not exist");
        }
    }

    @Override
    public LessonDTO update(int id, LessonDTO updated) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson with id " + id + " not found"));

        if (updated.getTitle() != null) {
            existingLesson.setTitle(updated.getTitle());
            existingLesson.setContent(updated.getContent());

        }
        Lesson updatedLesson = lessonRepository.save(existingLesson);
        return modelMapper.map(updatedLesson, LessonDTO.class);
    }

    @Override
    public void delete(int id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson with id " + id + " not found"));
        lessonRepository.delete(lesson);
    }
}
