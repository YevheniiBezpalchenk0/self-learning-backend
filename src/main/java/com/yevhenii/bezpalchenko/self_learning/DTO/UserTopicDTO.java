package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserTopicDTO {
    private int id;
    private TopicDTO topic;
    private UserDTO user;
    private List<LessonDTO> lessons;
    private int progress;
}
