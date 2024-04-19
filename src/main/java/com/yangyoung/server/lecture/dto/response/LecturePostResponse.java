package com.yangyoung.server.lecture.dto.response;

import com.yangyoung.server.lecture.domain.Lecture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LecturePostResponse {

    private Long id;

    private String name;

    private List<String> dayList;

    private LocalTime startTime;

    private LocalTime endTime;

    private String room;

    private List<String> sectionNameList;

    public LecturePostResponse(Lecture lecture, List<String> sectionNameList) {
        this.id = lecture.getId();
        this.name = lecture.getName();
        if (lecture.getDayList() != null) {
            this.dayList = lecture.getDayList().stream().
                    map(lectureDay -> lectureDay.getDay().getDayName()).
                    toList();
        }
        this.startTime = lecture.getStartTime();
        this.endTime = lecture.getEndTime();
        this.room = lecture.getLectureRoom();
        this.sectionNameList = sectionNameList;
    }

}
