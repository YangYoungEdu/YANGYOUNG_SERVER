package com.yangyoung.server.lectureDay.domain;

import com.yangyoung.server.day.domain.Day;
import com.yangyoung.server.lecture.domain.Lecture;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "lecture_day")
public class LectureDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne()
    @JoinColumn(name = "day_id")
    private Day day;

    public LectureDay(Lecture lecture, Day day) {
        this.lecture = lecture;
        this.day = day;
    }
}
