package com.yevhenii.bezpalchenko.self_learning.DTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLessonDTO {
    private int id;
    private UserDTO user;
    private LessonDTO lesson;
    private boolean completed;
    private int grade;
}
