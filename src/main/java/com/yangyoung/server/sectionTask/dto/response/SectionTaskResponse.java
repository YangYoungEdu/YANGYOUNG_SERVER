package com.yangyoung.server.sectionTask.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionTaskResponse {

    private Long id;

    private String assignment;

    private String sectionName;
}
