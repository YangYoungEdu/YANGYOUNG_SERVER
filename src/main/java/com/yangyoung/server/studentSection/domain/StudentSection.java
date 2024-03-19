package com.yangyoung.server.studentSection.domain;

import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.student.domain.Student;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StudentSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne()
    @JoinColumn(name = "section_id")
    private Section section;

    @Builder
    public StudentSection(Student student, Section section) {
        this.student = student;
        this.section = section;
    }

    public void updateSection(Section section) {
        this.section = section;
    }
}
