package com.yangyoung.server.lecture.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LectureSeqUpdateRequest {

    private List<LectureSeqUpdateRequestOne> lectureSeqList;
}