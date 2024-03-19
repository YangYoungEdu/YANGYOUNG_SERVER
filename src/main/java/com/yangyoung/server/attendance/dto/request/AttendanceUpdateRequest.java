package com.yangyoung.server.attendance.dto.request;

import com.yangyoung.server.attendance.domain.AttendanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceUpdateRequest {

    private LocalDateTime attendedDateTime;

    private AttendanceType attendanceType;

    private Long studentId;

    private String note;
}
