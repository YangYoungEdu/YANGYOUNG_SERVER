package com.yangyoung.server.studentTask.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentTaskResponse {

    private Long id;

    private String content;

    private LocalDate taskDate;

    private String taskProgress;
}
