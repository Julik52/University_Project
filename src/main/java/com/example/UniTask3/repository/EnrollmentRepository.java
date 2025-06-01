package com.example.UniTask3.repository;
import com.example.UniTask3.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  List<Enrollment> findByStudent_Id(Long studentId);
}