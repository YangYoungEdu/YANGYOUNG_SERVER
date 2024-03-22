package com.yangyoung.server.section.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yangyoung.server.attendance.domain.Attendance;
import com.yangyoung.server.configuration.BaseEntity;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import com.yangyoung.server.sectionTask.domain.SectionTask;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.studentSection.domain.StudentSection;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class
Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String teacher;

    private String homeRoom;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<StudentSection> studentSectionList;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<SectionLecture> sectionlectureList;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<SectionTask> sectionTaskList;

    @Builder
    public Section(Long id, String name, String teacher, String homeRoom) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.homeRoom = homeRoom;
    }

    @Builder
    public Section(String name, String teacher) {
        this.name = name;
        this.teacher = teacher;
    }
}
