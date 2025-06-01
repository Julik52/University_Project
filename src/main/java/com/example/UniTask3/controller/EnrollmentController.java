package com.example.UniTask3.controller;

import com.example.UniTask3.Service.EnrollmentService;
import com.example.UniTask3.Service.StudentService;
import com.example.UniTask3.dto.EnrollmentDto;
import com.example.UniTask3.model.Enrollment;
import com.example.UniTask3.model.Course;
import com.example.UniTask3.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

  private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

  @Autowired
  private EnrollmentService enrollmentService;

  @Autowired
  private StudentService studentService;

  @PostMapping
  public Enrollment enrollStudent(@RequestBody EnrollmentDto enrollmentDto) {
    logger.info("Enrolling student id={} to course id={}", enrollmentDto.getStudentId(), enrollmentDto.getCourseId());
    return enrollmentService.enrollStudent(enrollmentDto);
  }

  @GetMapping
  public List<EnrollmentDto> getAllEnrollments() {
    logger.info("Fetching all enrollments");
    return enrollmentService.getAllEnrollments();
  }

  @DeleteMapping("/{id}")
  public void deleteEnrollment(@PathVariable Long id) {
    logger.info("Deleting enrollment with id={}", id);
    enrollmentService.deleteEnrollment(id);
  }

  @GetMapping("/by-student/{studentId}")
  public ResponseEntity<?> getStudentCourses(@PathVariable Long studentId) {
    logger.info("Fetching courses for student id={}", studentId);
    Student student = studentService.getStudentById(studentId);
    if (student == null) {
      logger.error("Student not found with id={}", studentId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with id: " + studentId);
    }
    List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
    List<Course> courses = enrollments.stream()
        .map(Enrollment::getCourse)
        .collect(Collectors.toList());
    return ResponseEntity.ok(courses);
  }
}
