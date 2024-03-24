package com.yangyoung.server.section.dto.request;

import com.yangyoung.server.section.domain.Section;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SectionCreateRequest {

    private String name;

    private String teacher;

    private String homeRoom;

    private List<Long> studentIdList;

    public Section toEntity() {
        return Section.builder()
                .name(name)
                .teacher(teacher)
                .homeRoom(homeRoom)
                .build();
    }
}
