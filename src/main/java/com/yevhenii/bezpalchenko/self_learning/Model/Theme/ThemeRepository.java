package com.yevhenii.bezpalchenko.self_learning.Model.Theme;

import com.yevhenii.bezpalchenko.self_learning.Model.Course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Integer> {
    List<Theme> findByCourse(Course course);
}
