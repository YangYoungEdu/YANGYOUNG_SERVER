package com.yangyoung.server.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceAllResponse {

    private List<AttendanceResponse> attendanceList;

    private Integer attendanceCount;

}
