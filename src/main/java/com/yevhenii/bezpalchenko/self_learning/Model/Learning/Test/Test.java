package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Test;

import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson.Lesson;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Question.Question;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTest.UserTest;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String title;
    @OneToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTest> userTests;
}
