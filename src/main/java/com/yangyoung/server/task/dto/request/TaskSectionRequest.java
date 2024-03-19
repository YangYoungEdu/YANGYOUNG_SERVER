package com.yangyoung.server.task.dto.request;

import com.yangyoung.server.task.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSectionRequest {

    private String assignment;

    private Long sectionId;

    public Task toEntity() {
        return Task.builder().assignment(assignment).build();
    }
}
