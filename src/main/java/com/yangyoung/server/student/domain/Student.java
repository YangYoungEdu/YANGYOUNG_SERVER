package com.yangyoung.server.student.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yangyoung.server.configuration.BaseEntity;
import com.yangyoung.server.studentMaterial.domain.StudentMaterial;
import com.yangyoung.server.studentSection.domain.StudentSection;
import com.yangyoung.server.studentTask.domain.StudentTask;
import com.yangyoung.server.attendance.domain.Attendance;
import com.yangyoung.server.section.domain.Section;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Student extends BaseEntity {

    @Id
    private Long id;

    private String name;

    private String school;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private String studentPhoneNumber;

    private String parentPhoneNumber;

    //    @ManyToOne()
//    @JoinColumn(name = "section_id")
//    @JsonManagedReference
//    private Section section;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<StudentSection> studentSectionList;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<StudentTask> studentTaskList;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<StudentMaterial> studentMaterialList;

    public Student(Long id, String name, String school, Grade grade, String studentPhoneNumber) {
        this.id = id;
        this.name = name;
        this.school = school;
        this.grade = grade;
        this.studentPhoneNumber = studentPhoneNumber;
    }

    public void update(String school, Grade grade, String studentPhoneNumber, String parentPhoneNumber) {
        if (school != null && !school.isBlank()) {
            this.school = school;
        }
        if (grade != null) {
            this.grade = grade;
        }
        if (studentPhoneNumber != null && !studentPhoneNumber.isBlank()) {
            this.studentPhoneNumber = studentPhoneNumber;
        }
        if (parentPhoneNumber != null && !parentPhoneNumber.isBlank()) {
            this.parentPhoneNumber = parentPhoneNumber;
        }
    }
}
