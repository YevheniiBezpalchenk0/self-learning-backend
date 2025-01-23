package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TopicDTO {
    private int id;
    private String title;
    private List<LessonDTO> lessons;
}
