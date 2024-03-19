package com.yangyoung.server.student.dto.response;

import com.yangyoung.server.section.dto.response.SectionBriefResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentAllResponse {

    private List<StudentResponse> studentResponseList;

    private List<SectionBriefResponse> sectionList;

    private List<String> gradeList;

    private List<String> schoolList;

    private Integer size;

}
