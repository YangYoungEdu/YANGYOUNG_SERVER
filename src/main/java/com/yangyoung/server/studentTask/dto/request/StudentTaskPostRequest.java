package com.yangyoung.server.studentTask.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentTaskPostRequest {

    private Long taskId;

    private Long sectionId;
}
