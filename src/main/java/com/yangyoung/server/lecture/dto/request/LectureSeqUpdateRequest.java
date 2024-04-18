package com.yangyoung.server.lecture.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class LectureSeqUpdateRequest {

    private List<Map<Long, Long>> lectureSeqList;
}
