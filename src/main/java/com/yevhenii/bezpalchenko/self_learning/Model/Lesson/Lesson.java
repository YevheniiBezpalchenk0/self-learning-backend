package com.yevhenii.bezpalchenko.self_learning.Model.Lesson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yevhenii.bezpalchenko.self_learning.Model.Course.Course;
import com.yevhenii.bezpalchenko.self_learning.Model.Theme.Theme;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
@Table(name = "_lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private boolean completed;
    private double grade;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    @JsonBackReference
    private Theme theme;

    public void setCompleted() {
        this.completed = true;
        getTheme().checkCompleted();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
