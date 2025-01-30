package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserCourse;

import com.yevhenii.bezpalchenko.self_learning.Model.Learning.Course.Course;
import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {
    List<UserCourse> findByStudent(User user);

    Optional<UserCourse> findByCourseAndStudent(Course course, User user);
}
