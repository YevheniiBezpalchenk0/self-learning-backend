package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserLesson;


import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson.Lesson;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTest.UserTest;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTopic.UserTopic;
import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "user_lesson")
public class UserLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    @Column(nullable = false)
    private int grade;
    @Column(nullable = false)
    private boolean completed;
    @ManyToOne
    @JoinColumn(name = "user_topic_id", nullable = false)
    private UserTopic userTopic;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_test_id", referencedColumnName = "id")
    private UserTest userTest;

}
