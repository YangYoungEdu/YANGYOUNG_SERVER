package com.yangyoung.server.sectionLecture.dto.request;

import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnrollLectureCreateRequest {

    private Long sectionId;

    private Long lectureId;
}
