package com.yangyoung.server.lecture.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yangyoung.server.configuration.BaseEntity;
import com.yangyoung.server.lectureDate.domain.LectureDate;
import com.yangyoung.server.lectureDay.domain.LectureDay;
import com.yangyoung.server.lectureMaterial.domain.LectureMaterial;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String teacher;

    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LectureDay> dayList;

    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LectureDate> dateList;

    private LocalTime startTime;

    private LocalTime endTime;

    private String homeRoom;

    private String lectureRoom;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<SectionLecture> sectionLectureList;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<LectureMaterial> lectureMaterialList;

    @Builder
    public Lecture(String name, String teacher, LocalTime startTime, LocalTime endTime, String homeRoom, String lectureRoom, List<SectionLecture> sectionLectureList) {
        this.name = name;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;
        this.homeRoom = homeRoom;
        this.lectureRoom = lectureRoom;
        this.sectionLectureList = sectionLectureList;
    }

    public void updateLectureDay(List<LectureDay> dayList) {
        this.dayList = dayList;
    }

    public void updateLectureDate(List<LectureDate> dateList) {
        this.dateList = dateList;
    }
}

