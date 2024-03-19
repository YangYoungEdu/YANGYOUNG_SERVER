package com.yangyoung.server.attendance.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yangyoung.server.configuration.BaseEntity;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.student.domain.Student;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime attendedDateTime;

    @Enumerated(EnumType.STRING)
    private AttendanceType attendanceType;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonManagedReference
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonManagedReference
    private Section section;

    @Builder
    public Attendance(LocalDateTime attendedDateTime, AttendanceType attendanceType, String note,
                      Student student, Section section) {
        this.attendedDateTime = attendedDateTime;
        this.attendanceType = attendanceType;
        this.note = note;
        this.student = student;
        this.section = section;
    }

    @Builder
    public Attendance(Long id, LocalDateTime attendedDateTime, AttendanceType attendanceType, String note,
                      Student student, Section section) {
        this.id = id;
        this.attendedDateTime = attendedDateTime;
        this.attendanceType = attendanceType;
        this.note = note;
        this.student = student;
        this.section = section;
    }

    public void update(AttendanceType attendanceType, String note) {
        this.attendanceType = attendanceType;
        this.note = note;
    }

    public void updateAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }
}
