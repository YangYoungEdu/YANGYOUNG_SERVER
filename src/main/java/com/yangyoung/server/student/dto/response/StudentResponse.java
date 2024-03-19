package com.yangyoung.server.student.dto.response;

import com.yangyoung.server.student.domain.Grade;
import com.yangyoung.server.student.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private Long id;

    private String name;

    private String school;

    private String grade;

    private String studentPhoneNumber;

    private String parentPhoneNumber;

    private String sectionName;

    private Long sectionId;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.school = student.getSchool();
        this.grade = student.getGrade().name();
        this.studentPhoneNumber = student.getStudentPhoneNumber();
        this.parentPhoneNumber = student.getParentPhoneNumber();
        this.sectionName = student.getSection().getName();
        this.sectionId = student.getSection().getId();
    }
}
