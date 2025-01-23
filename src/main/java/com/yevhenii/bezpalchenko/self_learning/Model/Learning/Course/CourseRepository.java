package com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course;

import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByTitle(String title);

    Optional<Course> findAllByCreator(User creator);
}