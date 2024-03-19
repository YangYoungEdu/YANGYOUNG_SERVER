package com.yangyoung.server.section.dto.response;

import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.sectionTask.dto.response.SectionTaskAllResponse;
import com.yangyoung.server.student.dto.response.StudentAllResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDetailResponse {

    private SectionResponse sectionResponse;

    private LectureAllResponse lectureAllResponse;

    private StudentAllResponse studentAllResponse;

    private SectionTaskAllResponse sectionTaskAllResponse;
}
