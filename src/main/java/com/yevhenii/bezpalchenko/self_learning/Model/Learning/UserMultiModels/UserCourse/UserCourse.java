package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserCourse;


import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course.Course;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTopic.UserTopic;
import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "user_course")
public class UserCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private double grade;

    @OneToMany(mappedBy = "userCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTopic> userTopics;

}
