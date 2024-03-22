package com.yangyoung.server.section.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SectionUpdateRequest {

    private Long sectionId;

    private String name;

    private String teacher;

    private String homeRoom;
}
