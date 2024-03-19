package com.yangyoung.server.studentTask.dto.response;

import com.yangyoung.server.studentTask.domain.TaskProgress;
import com.yangyoung.server.task.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentTaskResponse {

    private Long id;

    private String taskName;

    private String taskProgress;
}
