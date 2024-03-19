package com.yangyoung.server.attendance.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStudentRequest {

//    private LocalDateTime attendedDateTime;

    private Long studentId;

    private String note;

}
