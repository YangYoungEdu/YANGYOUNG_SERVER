package com.yangyoung.server.lecture.dto.request;

import com.yangyoung.server.lecture.domain.Lecture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureCreateRequest {

    private String name;

    private String teacher;

    private List<String> dayList;

    private LocalTime startTime;

    private LocalTime endTime;

    private String homeRoom;

    private String lectureRoom;

    private List<Long> sectionIdList;

    public Lecture toEntity() {
        return Lecture.builder()
                .name(name)
                .teacher(teacher)
                .startTime(startTime)
                .endTime(endTime)
                .homeRoom(homeRoom)
                .lectureRoom(lectureRoom)
                .build();
    }
}
