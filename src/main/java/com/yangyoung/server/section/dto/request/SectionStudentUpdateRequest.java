package com.yangyoung.server.section.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SectionStudentUpdateRequest {

    private Long sectionId;

    private List<Long> studentIdList;
}
