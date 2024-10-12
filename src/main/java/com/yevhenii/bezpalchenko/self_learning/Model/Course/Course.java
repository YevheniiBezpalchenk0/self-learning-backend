package com.yevhenii.bezpalchenko.self_learning.Model.Course;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yevhenii.bezpalchenko.self_learning.Model.Theme.Theme;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "_course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private boolean completed;
    private double grade;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Theme> themes;
    public void setCompleted() {
        this.completed = true;
    }

    public void checkCompleted(){
        if(isCompleted()) {
            return;
        }
        boolean isAllThemesCompleted  = true;
        for (var theme: getThemes()
        ) {
            if (!theme.isCompleted()) {
                isAllThemesCompleted  = false;
                break;
            }
        }
        if(isAllThemesCompleted ) {
            setCompleted();
            checkGrade();
        }
    }
    private void checkGrade(){
        double gradeVar = 0;
        for (var theme: getThemes()
             ) {
            gradeVar += theme.getGrade();
        }
        setGrade(gradeVar);
    }
    private void setGrade(double newGrade){
        this.grade = newGrade;
    }
}
