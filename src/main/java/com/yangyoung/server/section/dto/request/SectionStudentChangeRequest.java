package com.yangyoung.server.section.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SectionStudentChangeRequest {

    private Long studentId;

    private List<Long> sectionIdLIst;
}
