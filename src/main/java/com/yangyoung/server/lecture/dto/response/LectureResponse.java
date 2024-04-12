package com.yangyoung.server.lecture.dto.response;

import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lectureDate.domain.LectureDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private List<LocalDate> dateList;

    private LocalTime startTime;

    private LocalTime endTime;

    private String homeRoom;

    private String lectureRoom;

    private List<String> sectionNameList;


    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.teacher = lecture.getTeacher();
        this.name = lecture.getName();
        this.dayList = lecture.getDayList().stream().
                map(lectureDay -> lectureDay.getDay().getDayName()).
                toList();
        this.dateList = lecture.getDateList().stream()
                .map(LectureDate::getDate)
                .toList();
        this.startTime = lecture.getStartTime();
        this.endTime = lecture.getEndTime();
        this.lectureRoom = lecture.getLectureRoom();
        for (int i = 0; i < lecture.getSectionLectureList().size(); i++) {
            if (lecture.getSectionLectureList().get(i).getLecture().getName().equals(lecture.getName())) {
                this.homeRoom = lecture.getSectionLectureList().get(i).getSection().getHomeRoom();
                break;
            }
        }
        this.sectionNameList = lecture.getSectionLectureList().stream()
                .map(sectionLecture -> sectionLecture.getSection().getName())
                .toList();
//        this.homeRoom = lecture.getSectionLectureList().stream()
//                .filter(sectionLecture -> sectionLecture.getLecture().getName().equals(lecture.getName()))
//                .map(sectionLecture -> sectionLecture.getSection().getHomeRoom())
//                .findAny()
//                .orElse(null);
    }
}
