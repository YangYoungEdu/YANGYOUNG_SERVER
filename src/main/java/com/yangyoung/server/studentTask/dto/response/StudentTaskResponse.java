package com.yangyoung.server.studentTask.dto.response;

import com.yangyoung.server.student.dto.response.StudentResponse;
import com.yangyoung.server.studentTask.domain.StudentTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentTaskResponse {

    private Long id;

    private String content;

    private LocalDate taskDate;

    private String taskProgress;

    private String taskType;

    public StudentTaskResponse(StudentTask studentTask) {
        this.id = studentTask.getId();
        this.content = studentTask.getTask().getContent();
        this.taskDate = studentTask.getTask().getTaskDate();
        this.taskProgress = studentTask.getTaskProgress().getProgressName();
        this.taskType = studentTask.getTask().getTaskType().getTypeName();
    }
}
