package com.yangyoung.server.task.dto.request;

import com.yangyoung.server.task.domain.Task;
import com.yangyoung.server.task.domain.TaskType;
import jakarta.annotation.security.DenyAll;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@DenyAll
public class TaskStudentRequest {

    private Long studentId;

    private String content;

    private LocalDate taskDate;

    public Task toEntity() {
        return Task.builder().
                content(content).
                taskDate(taskDate).
                taskType(TaskType.STUDENT).
                build();
    }
}
