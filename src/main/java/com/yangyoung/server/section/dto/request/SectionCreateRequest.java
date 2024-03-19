package com.yangyoung.server.section.dto.request;

import com.yangyoung.server.section.domain.Section;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SectionCreateRequest {

    private String name;

    private String teacher;

    public Section toEntity() {
        return Section.builder()
                .name(name)
                .teacher(teacher)
                .build();
    }
}
