package com.yevhenii.bezpalchenko.self_learning.Model.Theme;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yevhenii.bezpalchenko.self_learning.Model.Course.Course;
import com.yevhenii.bezpalchenko.self_learning.Model.Lesson.Lesson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
@Table(name = "_theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private boolean completed;
    private double grade;
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Course course;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Lesson> lessons;

    public void setCompleted() {
        this.completed = true;
    }
    public void checkCompleted(){
        if(isCompleted()) {
            return;
        }
        boolean isAllLessonsCompleted  = true;
        for (var lesson: getLessons()
        ) {
            if (!lesson.isCompleted()) {
                isAllLessonsCompleted = false;
                break;
            }
        }
        if(isAllLessonsCompleted) {
            setCompleted();
            checkGrade();
            getCourse().checkCompleted();
        }
    }
    private void checkGrade(){
        double gradeVar = 0;
        for (var lesson: getLessons()
        ) {
            gradeVar += lesson.getGrade();
        }
        setGrade(gradeVar);
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
