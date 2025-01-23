package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTopic;

import com.yevhenii.bezpalchenko.self_learning.DTO.UserTopicDTO;
import com.yevhenii.bezpalchenko.self_learning.Model.IService;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserCourse.UserCourse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTopicService implements IService<UserTopicDTO> {

    private final UserTopicRepository userTopicRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserTopicDTO> getAll() {
        return null;
    }

    @Override
    public Optional<UserTopicDTO> getById(int id) {
        return Optional.empty();
    }

    @Override
    public UserTopicDTO update(int id, UserTopicDTO updated) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
    public void createUserTopic(UserTopicDTO userTopicDTO, UserCourse userCourse) {
        UserTopic userTopic = modelMapper.map(userTopicDTO, UserTopic.class);
        userTopic.setUserCourse(userCourse);
        userTopicRepository.save(userTopic);
    }
}
