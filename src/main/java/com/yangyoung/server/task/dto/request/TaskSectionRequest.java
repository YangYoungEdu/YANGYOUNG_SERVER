package com.yangyoung.server.task.dto.request;

import com.yangyoung.server.task.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSectionRequest {

    private String content;

    private LocalDate taskDate;

    private Long sectionId;

    public Task toEntity() {
        return Task.builder().
                content(content).
                taskDate(taskDate).
                build();
    }
}
