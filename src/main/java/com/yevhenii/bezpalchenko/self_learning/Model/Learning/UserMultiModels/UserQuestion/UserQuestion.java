package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserQuestion;

import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.Answer;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.Question;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTest.UserTest;
import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "user_question")
public class UserQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_test_id", nullable = false)
    private UserTest userTest;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer chosenAnswer;

    @Column(nullable = false)
    private int score;
}