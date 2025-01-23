package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question;

import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserQuestion.UserQuestion;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Test.Test;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String text;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
    @Column(nullable = false)
    private int points;
    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuestion> userQuestions;

    public boolean isCorrect(Answer chosen) {
        for (Answer answer : answers) {
            if (chosen.equals(answer) && chosen.isCorrect()) {
                return true;
            }
        }
        return false;
    }
}
