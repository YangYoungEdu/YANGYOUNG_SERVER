package com.yangyoung.server.section.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionAllBriefResponse {

    private List<SectionBriefResponse> sectionBriefResponses;

    private Integer sectionCount;
}
