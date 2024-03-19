package com.yangyoung.server.section.dto.response;

import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionLectureResponse {

    private SectionResponse sectionResponse;

    private LectureAllResponse lectureAllResponse;

}
