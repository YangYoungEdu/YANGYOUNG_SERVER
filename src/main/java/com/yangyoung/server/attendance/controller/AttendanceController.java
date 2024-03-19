package com.yangyoung.server.attendance.controller;

import com.yangyoung.server.attendance.dto.request.AttendanceStudentRequest;
import com.yangyoung.server.attendance.dto.request.AttendanceSectionRequest;
import com.yangyoung.server.attendance.dto.response.AttendanceAllResponse;
import com.yangyoung.server.attendance.dto.response.AttendanceResponse;
import com.yangyoung.server.attendance.service.AttendanceService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 직접 출석 체크 컨트롤러 (학생)
    @PostMapping("")
    public ResponseEntity<AttendanceResponse> postAttendance(@RequestBody final AttendanceStudentRequest request) {

        final AttendanceResponse response = attendanceService.attend(request);

        return ResponseEntity.ok()
                .body(response);
    }

    // 출석 체크 컨트롤러 (반)
    @ApiOperation(value = "출석 체크", notes = "출석 체크 API")
    @PatchMapping("")
    public ResponseEntity<AttendanceAllResponse> postAttendanceBySection(@RequestBody final AttendanceSectionRequest request) {

        final AttendanceAllResponse response = attendanceService.attendBySection(request);

        return ResponseEntity.ok().body(response);
    }

    // 출석 조회 컨트롤러 (학생)
    @GetMapping("/student/{studentId}")
    public ResponseEntity<AttendanceResponse> getAllAttendancesByStudent(@PathVariable final Long studentId, @RequestParam final LocalDateTime selectedDay) {

        final AttendanceResponse response = attendanceService.getAttendanceByStudent(studentId, selectedDay);

        return ResponseEntity.ok()
                .body(response);
    }

    // 출석 조회 컨트롤러 (반)
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<AttendanceAllResponse> getAllAttendancesBySection(@PathVariable final Long sectionId, @RequestParam final LocalDateTime selectedDay) {

        final AttendanceAllResponse response = attendanceService.getAllAttendancesBySection(sectionId, selectedDay);

        return ResponseEntity.ok()
                .body(response);
    }
}
