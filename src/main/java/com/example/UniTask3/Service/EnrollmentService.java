package com.example.UniTask3.Service;

import com.example.UniTask3.dto.EnrollmentDto;
import com.example.UniTask3.model.Course;
import com.example.UniTask3.model.Enrollment;
import com.example.UniTask3.model.Student;
import com.example.UniTask3.repository.CourseRepository;
import com.example.UniTask3.repository.EnrollmentRepository;
import com.example.UniTask3.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnrollmentService {

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository courseRepository;

  public Enrollment enrollStudent(EnrollmentDto enrollmentDto) {
    if (enrollmentDto.getStudentId() == null) {
      throw new IllegalArgumentException("StudentId must not be null");
    }
    if (enrollmentDto.getCourseId() == null) {
      throw new IllegalArgumentException("CourseId must not be null");
    }

    Student student = studentRepository.findById(enrollmentDto.getStudentId())
        .orElseThrow(() -> new RuntimeException("Student not found with id: " + enrollmentDto.getStudentId()));

    Course course = courseRepository.findById(enrollmentDto.getCourseId())
        .orElseThrow(() -> new RuntimeException("Course not found with id: " + enrollmentDto.getCourseId()));

    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setCourse(course);
    enrollment.setEnrollmentDate(enrollmentDto.getEnrollmentDate());

    return enrollmentRepository.save(enrollment);
  }


  public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
    return enrollmentRepository.findByStudent_Id(studentId);
  }

  public void deleteEnrollment(Long id) {
    enrollmentRepository.deleteById(id);
  }

  public List<EnrollmentDto> getAllEnrollments() {
    List<Enrollment> enrollments = enrollmentRepository.findAll();
    return enrollments.stream().map(e -> new EnrollmentDto(
        e.getId(),
        e.getStudent().getId(),
        e.getCourse().getId(),
        e.getEnrollmentDate()
    )).toList();
  }

}
