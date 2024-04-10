package com.yangyoung.server.student.dto.request;

import com.yangyoung.server.student.domain.Grade;
import com.yangyoung.server.student.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreateRequest {

    private Long id;

    private String name;

    private String school;

    private Grade grade;

    private String studentPhoneNumber;

    private String parentPhoneNumber;

    private List<Long> sectionIdList;

    public Student toEntity() {
        return Student.builder()
                .id(id)
                .name(name)
                .school(school)
                .grade(grade)
                .studentPhoneNumber(studentPhoneNumber)
                .parentPhoneNumber(parentPhoneNumber)
                .build();
    }
}
