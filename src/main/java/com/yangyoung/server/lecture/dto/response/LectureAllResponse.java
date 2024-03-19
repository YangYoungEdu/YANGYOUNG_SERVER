package com.yangyoung.server.lecture.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureAllResponse {

    private List<LectureResponse> lectureResponseList;

    private Integer lectureCount;

}
