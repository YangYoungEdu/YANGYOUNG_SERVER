package com.yangyoung.server.sectionTask.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionTaskResponse {

    private Long id;

    private String content;

    private LocalDate date;

    private String taskType;

}
