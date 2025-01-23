package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class CourseDTO {
    private int id;
    private String title;
    private String description;
    private UserDTO creator;
    private List<TopicDTO> topics;
}
