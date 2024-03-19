package com.yangyoung.server.section.dto.response;

import com.yangyoung.server.section.domain.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionBriefResponse {

    private Long id;

    private String name;

    public SectionBriefResponse(Section section) {
        this.id = section.getId();
        this.name = section.getName();
    }
}
