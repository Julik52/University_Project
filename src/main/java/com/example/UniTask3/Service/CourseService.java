package com.example.UniTask3.Service;

import com.example.UniTask3.model.Course;
import com.example.UniTask3.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  public List<Course> getAllCourses() {
    return courseRepository.findAll();
  }

  public Course getCourseById(Long id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
  }

  public Course createCourse(Course course) {
    return courseRepository.save(course);
  }

  public void deleteCourse(Long id) {
    courseRepository.deleteById(id);
  }

  public Course updateCourse(Long id, Course updatedCourse) {
    Course course = getCourseById(id);
    course.setTitle(updatedCourse.getTitle());
    course.setCredits(updatedCourse.getCredits());
    return courseRepository.save(course);
  }

}
