package com.yangyoung.server.sectionTask.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionTaskAllResponse {

    private List<SectionTaskResponse> sectionTaskResponseList;

    private Integer sectionTaskSize;
}
