package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTopic;

import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Topic.Topic;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserCourse.UserCourse;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserLesson.UserLesson;
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
public class UserTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "userCourse_id", nullable = false)
    private UserCourse userCourse;

    @Column(nullable = false)
    private double grade;

    @Column(nullable = false)
    private double completed;

    @OneToMany(mappedBy = "userTopic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLesson> userLessons;
}
