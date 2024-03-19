package com.yangyoung.server.attendance.domain;

public enum AttendanceType {
    ATTENDANCE(1, "출석"), // 출석
    ABSENCE(2, "결석"),    // 결석
    LATENESS(3, "지각");    // 지각

    private final int attendanceTypeNum;

    private final String attendanceTypeName;

    AttendanceType(int attendanceTypeNum, String attendanceTypeName) {
        this.attendanceTypeNum = attendanceTypeNum;
        this.attendanceTypeName = attendanceTypeName;
    }


}

