package com.example.UniTask3.controller;

import com.example.UniTask3.Service.StudentService;
import com.example.UniTask3.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

  private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

  @Autowired
  private StudentService studentService;


  @GetMapping
  public List<Student> getAllStudents() {
    logger.info("Fetching all students");
    return studentService.getAllStudents();
  }

  @GetMapping("/{id}")
  public Student getStudentById(@PathVariable Long id) {
    logger.info("Fetching student with id={}", id);
    return studentService.getStudentById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Student createStudent(@RequestBody Student student) {
    logger.info("Creating student: {}", student.getName());
    return studentService.createStudent(student);
  }

  @PutMapping("/{id}")
  public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
    logger.info("Updating student id={} with name={}", id, student.getName());
    return studentService.updateStudent(id, student);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteStudent(@PathVariable Long id) {
    logger.info("Deleting student with id={}", id);
    studentService.deleteStudent(id);
  }
}
