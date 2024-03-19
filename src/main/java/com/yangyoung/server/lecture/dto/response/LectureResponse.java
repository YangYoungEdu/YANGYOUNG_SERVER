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
public class LectureResponse {

    private Long id;

    private String teacher;

    private String name;

    private List<String> dayList;

    private LocalTime startTime;

    private LocalTime endTime;

    private String room;

    private String sectionName;

    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.teacher = lecture.getTeacher();
        this.name = lecture.getName();
        this.dayList = lecture.getDayList().stream().
                map(lectureDay -> lectureDay.getDay().getDayName()).
                toList();
        this.startTime = lecture.getStartTime();
        this.endTime = lecture.getEndTime();
        this.room = lecture.getRoom();
        this.sectionName = lecture.getSectionLectureList().get(0).getSection().getName();
    }
}
