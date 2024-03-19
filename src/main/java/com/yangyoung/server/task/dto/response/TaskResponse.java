package com.yangyoung.server.task.dto.response;

import com.yangyoung.server.task.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String content;

    private LocalDate taskDate;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.content = task.getContent();
        this.taskDate = task.getTaskDate();
    }
}
