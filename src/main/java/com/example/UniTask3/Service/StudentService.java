package com.example.UniTask3.Service;

import com.example.UniTask3.model.Student;
import com.example.UniTask3.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

  @Autowired
  private StudentRepository studentRepository;

  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }

  public Student getStudentById(Long id) {
    return studentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
  }

  public Student createStudent(Student student) {
    return studentRepository.save(student);
  }

  public void deleteStudent(Long id) {
    studentRepository.deleteById(id);
  }

  public Student updateStudent(Long id, Student updatedStudent) {
    Student student = getStudentById(id);
    student.setName(updatedStudent.getName());
    student.setEmail(updatedStudent.getEmail());
    student.setGroupName(updatedStudent.getGroupName());
    return studentRepository.save(student);
  }
}
