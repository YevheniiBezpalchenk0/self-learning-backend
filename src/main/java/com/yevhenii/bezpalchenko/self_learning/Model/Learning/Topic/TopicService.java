package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Topic;

import com.yevhenii.bezpalchenko.self_learning.DTO.CourseDTO;
import com.yevhenii.bezpalchenko.self_learning.DTO.TopicDTO;
import com.yevhenii.bezpalchenko.self_learning.Exception.EntityAlreadyExistsException;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course.Course;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService implements IService<TopicDTO> {
    private final TopicRepository topicRepository;
    private final CourseService courseService;
    private final ModelMapper modelMapper;

    @Override
    public List<TopicDTO> getAll() {
        return topicRepository.findAll()
                .stream()
                .map(topic -> modelMapper.map(topic, TopicDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TopicDTO> getById(int id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic with id " + id + " not found"));
        return Optional.of(modelMapper.map(topic, TopicDTO.class));
    }

    public TopicDTO create(Topic topic, int courseId) throws EntityAlreadyExistsException, EntityNotFoundException {
        Optional<Topic> existingTopic = topicRepository.findByTitle(topic.getTitle());
        if (existingTopic.isPresent()) {
            throw new EntityAlreadyExistsException("Topic with that name already presents: " + topic.getTitle());
        }

        Optional<CourseDTO> courseDTOOptional = courseService.getById(courseId);
        if (courseDTOOptional.isPresent()) {
            Course course = modelMapper.map(courseDTOOptional.get(), Course.class);
            topic.setCourse(course);
            Topic savedTopic = topicRepository.save(topic);
            return modelMapper.map(savedTopic, TopicDTO.class);
        } else {
            throw new EntityNotFoundException("A course with id" + courseId + " does not exist");
        }
    }

    @Override
    public TopicDTO update(int id, TopicDTO updated) {
        Topic existingTopic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic with id " + id + " not found"));

        if (updated.getTitle() != null) {
            existingTopic.setTitle(updated.getTitle());
        }
        Topic updatedTopic = topicRepository.save(existingTopic);
        return modelMapper.map(updatedTopic, TopicDTO.class);
    }

    @Override
    public void delete(int id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic with id " + id + " not found"));
        topicRepository.delete(topic);
    }
}
