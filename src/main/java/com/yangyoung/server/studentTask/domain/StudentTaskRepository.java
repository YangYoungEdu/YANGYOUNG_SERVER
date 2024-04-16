package com.yangyoung.server.studentTask.domain;

import com.yangyoung.server.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {

    List<StudentTask> findByStudentId(Long studentId);

    List<StudentTask> findByStudentIdAndTask_TaskDate(Long studentId, LocalDate taskDate);

    StudentTask findByStudentIdAndTaskId(Long studentId, Long taskId);

}
