package com.yangyoung.server.section.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionAllResponse {

    private List<SectionResponse> sectionResponseList;

    private Integer size;

}
