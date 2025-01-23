package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTest;

import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserLesson.UserLesson;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserQuestion.UserQuestion;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Test.Test;
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
@Table(name = "user_test")
public class UserTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
    @OneToMany(mappedBy = "userTest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuestion> userQuestions;

    @OneToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private UserLesson userLesson;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private boolean passed;
}