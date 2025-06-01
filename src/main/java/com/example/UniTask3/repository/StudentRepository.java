package com.example.UniTask3.repository;

import com.example.UniTask3.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
