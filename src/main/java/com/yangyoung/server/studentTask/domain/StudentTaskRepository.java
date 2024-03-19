package com.yangyoung.server.studentTask.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {

    List<StudentTask> findByStudentId(Long studentId);

    StudentTask findByStudentIdAndTaskId(Long studentId, Long taskId);

    List<StudentTask> findByStudentIdAndTask_TaskDate(Long studentId, LocalDate taskDate);
}
