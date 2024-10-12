package com.yevhenii.bezpalchenko.self_learning.Model.Lesson;

import com.yevhenii.bezpalchenko.self_learning.Model.Theme.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByTheme(Theme theme);
}
