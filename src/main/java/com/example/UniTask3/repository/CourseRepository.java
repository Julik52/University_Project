package com.example.UniTask3.repository;
import com.example.UniTask3.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}