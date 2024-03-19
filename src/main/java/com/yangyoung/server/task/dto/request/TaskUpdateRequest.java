package com.yangyoung.server.task.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TaskUpdateRequest {

    private Long taskId;

    private String content;

    private LocalDate taskDate;

}
