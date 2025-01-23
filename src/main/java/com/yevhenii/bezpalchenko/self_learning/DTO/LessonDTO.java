package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LessonDTO {
    private int id;
    private String title;
    private String content;
    private TestDTO test;
}
