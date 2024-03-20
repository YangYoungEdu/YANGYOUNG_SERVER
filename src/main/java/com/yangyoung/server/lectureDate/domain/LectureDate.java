package com.yangyoung.server.lectureDate.domain;

import com.yangyoung.server.lecture.domain.Lecture;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "lecture_date")
public class LectureDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private LocalDate date;
}
