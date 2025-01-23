package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCourseDTO {
    private int id;
    private UserDTO user;
    private CourseDTO course;
    private int grade;
}
