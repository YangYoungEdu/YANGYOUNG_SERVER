package com.yangyoung.server.student.dto.request;

import com.yangyoung.server.student.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequest {

    private Long studentId;

    private String school;

    private Grade grade;

    private String studentPhoneNumber;

    private String parentPhoneNumber;

    private List<Long> sectionIdList;
}
