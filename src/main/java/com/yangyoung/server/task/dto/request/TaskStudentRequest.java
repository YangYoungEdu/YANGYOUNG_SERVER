package com.yangyoung.server.task.dto.request;

import com.yangyoung.server.task.domain.Task;
import jakarta.annotation.security.DenyAll;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@DenyAll
public class TaskStudentRequest {

    private String assignment;

    private Long studentId;

    public Task toEntity() {
        return Task.builder().assignment(assignment).build();
    }
}
