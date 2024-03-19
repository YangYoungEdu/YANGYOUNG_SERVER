package com.yangyoung.server.student.dto.request;

import com.yangyoung.server.student.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreationRequest {

    private Long id;

    private String name;

    private String school;

    private Grade grade;

    private String studentPhoneNumber;

    private String parentPhoneNumber;

    private Long sectionId;

}
