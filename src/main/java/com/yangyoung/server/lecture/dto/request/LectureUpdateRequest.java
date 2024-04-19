package com.yangyoung.server.lecture.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class LectureUpdateRequest {

    private Long id;

    private String name;

    private String teacher;

    private List<String> dayList;

    private List<LocalDate> dateList;

    private LocalTime startTime;

    private LocalTime endTime;

    private String lectureRoom;
}
