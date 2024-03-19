package com.yangyoung.server.attendance.dto.request;

import  lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSectionRequest {

    private Long sectionId;

    private List<AttendanceUpdateRequest> attendanceUpdateRequestList;

}
