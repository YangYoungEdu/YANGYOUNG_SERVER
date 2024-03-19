package com.yangyoung.server.task.dto.request;

import com.yangyoung.server.studentTask.domain.TaskProgress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskProgressUpdateRequest {

    private Long taskId;

    private Long studentId;

    private TaskProgress taskProgress;

}
