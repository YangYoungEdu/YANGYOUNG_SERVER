package com.yangyoung.server.student.dto.response;

import com.yangyoung.server.student.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentBriefResponse {

    private Long id;

    private String name;

    private String school;

    private String grade;

    private String studentPhoneNumber;

    private String parentPhoneNumber;


    public StudentBriefResponse(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.school = student.getSchool();
        this.grade = student.getGrade().name();
        this.studentPhoneNumber = student.getStudentPhoneNumber();
        this.parentPhoneNumber = student.getParentPhoneNumber();
    }
}
