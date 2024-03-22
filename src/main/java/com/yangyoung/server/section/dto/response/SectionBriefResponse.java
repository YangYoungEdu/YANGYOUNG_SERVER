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

    private String teacher;

    private String homeRoom;

    public SectionBriefResponse(Section section) {
        this.id = section.getId();
        this.name = section.getName();
        this.teacher = section.getTeacher();
        this.homeRoom = section.getHomeRoom();
    }
}
