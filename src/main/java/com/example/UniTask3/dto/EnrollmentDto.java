package com.example.UniTask3.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class EnrollmentDto {

  @Setter
  private Long id;
  private final Long studentId;
  private final Long courseId;
  private final LocalDate enrollmentDate;

  @JsonCreator
  public EnrollmentDto(
      @JsonProperty("id") Long id,
      @JsonProperty("studentId") Long studentId,
      @JsonProperty("courseId") Long courseId,
      @JsonProperty("enrollmentDate") LocalDate enrollmentDate) {
    this.id = id;
    this.studentId = studentId;
    this.courseId = courseId;
    this.enrollmentDate = enrollmentDate;
  }

}
