package com.example.UniTask3.controller;

import com.example.UniTask3.Service.CourseService;
import com.example.UniTask3.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

  private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

  @Autowired
  private CourseService courseService;

  @GetMapping
  public List<Course> getAllCourses() {
    logger.info("Fetching all courses");
    return courseService.getAllCourses();
  }

  @GetMapping("/{id}")
  public Course getCourseById(@PathVariable Long id) {
    logger.info("Fetching course with id={}", id);
    return courseService.getCourseById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Course createCourse(@RequestBody Course course) {
    logger.info("Creating new course: {}", course.getTitle());
    return courseService.createCourse(course);
  }

  @PutMapping("/{id}")
  public Course updateCourse(@PathVariable Long id, @RequestBody Course course) {
    logger.info("Updating course id={} with data: {}", id, course.getTitle());
    return courseService.updateCourse(id, course);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCourse(@PathVariable Long id) {
    logger.info("Deleting course with id={}", id);
    courseService.deleteCourse(id);
  }
}

