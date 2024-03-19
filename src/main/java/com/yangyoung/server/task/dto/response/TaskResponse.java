package com.yangyoung.server.task.dto.response;

import com.yangyoung.server.studentTask.domain.TaskProgress;
import com.yangyoung.server.task.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String assignment;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.assignment = task.getAssignment();
    }
}
