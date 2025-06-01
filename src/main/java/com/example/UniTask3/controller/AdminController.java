package com.example.UniTask3.controller;

import com.example.UniTask3.Service.CourseService;
import com.example.UniTask3.Service.EnrollmentService;
import com.example.UniTask3.Service.StudentService;
import com.example.UniTask3.dto.EnrollmentDto;
import com.example.UniTask3.model.Course;
import com.example.UniTask3.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

  @Autowired
  private StudentService studentService;

  @Autowired
  private CourseService courseService;

  @Autowired
  private EnrollmentService enrollmentService;

  @GetMapping("/students")
  public List<Student> getAllStudents() {
    logger.info("Admin requested list of all students");
    return studentService.getAllStudents();
  }

  @GetMapping("/courses")
  public List<Course> getAllCourses() {
    logger.info("Admin requested list of all courses");
    return courseService.getAllCourses();
  }

  @GetMapping("/enrollments")
  public List<EnrollmentDto> getAllEnrollments() {
    logger.info("Admin requested list of all enrollments");
    return enrollmentService.getAllEnrollments();
  }
}

