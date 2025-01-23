package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course;


import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserCourse.UserCourse;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Topic.Topic;
import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCourse> userCourses;

}