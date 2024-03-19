package com.yangyoung.server.attendance.dto.response;

import com.yangyoung.server.attendance.domain.Attendance;
import com.yangyoung.server.attendance.domain.AttendanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private LocalDateTime attendedDateTime;

    private Long studentId;

    private String studentName;

    private String sectionName;

    private String studentPhoneNumber;

    private String parentPhoneNumber;

    private AttendanceType attendanceType;

    private String note;

    public AttendanceResponse(Attendance attendance) {
        this.attendedDateTime = attendance.getAttendedDateTime();
        this.studentId = attendance.getStudent().getId();
        this.studentName = attendance.getStudent().getName();
        this.studentPhoneNumber = attendance.getStudent().getStudentPhoneNumber();
        this.parentPhoneNumber = attendance.getStudent().getParentPhoneNumber();
        this.sectionName = attendance.getSection().getName();
        this.attendanceType = attendance.getAttendanceType();
        this.note = attendance.getNote();
    }
}
