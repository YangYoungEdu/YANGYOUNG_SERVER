package com.yangyoung.server.sectionLecture.dto.response;

import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.section.domain.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollLecturePostResponse {

    private Section section;

    private Lecture lecture;

}
