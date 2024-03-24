package com.yangyoung.server.task.dto.request;

import com.yangyoung.server.task.domain.Task;
import com.yangyoung.server.task.domain.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class TaskMultipleSectionRequest {

    private String content;

    private LocalDate taskDate;

    private List<Long> sectionIdList;

    public Task toEntity() {
        return Task.builder().
                content(content).
                taskDate(taskDate).
                taskType(TaskType.SECTION).
                build();
    }
}
