package com.yangyoung.server.section.dto.response;

import com.yangyoung.server.student.dto.response.StudentAllResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionStudentResponse {

    private SectionResponse sectionResponse;

    private StudentAllResponse studentAllResponse;

}
