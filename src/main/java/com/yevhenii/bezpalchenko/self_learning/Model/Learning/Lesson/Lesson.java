package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Lesson;

import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Test.Test;
import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Topic.Topic;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
    @Column(length = 5000)
    private String content;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
