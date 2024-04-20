package com.yangyoung.server.student.controller;

import com.yangyoung.server.student.dto.request.StudentCreateRequest;
import com.yangyoung.server.student.dto.request.StudentUpdateRequest;
import com.yangyoung.server.student.dto.response.*;
import com.yangyoung.server.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/student")
@Slf4j
public class StudentController {

    private final StudentService studentService;

    // 학생 생성 컨트롤러
    @PostMapping("")
    public ResponseEntity<StudentResponse> createStudent(@RequestBody final StudentCreateRequest request) {

        final StudentResponse response = studentService.createStudent(request);

        return ResponseEntity.ok()
                .body(response);
    }

    // 학생 목록 조회 컨트롤러
    @GetMapping("")
    public ResponseEntity<StudentAllResponse> readAllStudents() {

        final StudentAllResponse response = studentService.readAllStudents();

        return ResponseEntity.ok()
                .body(response);

    }

    // 학생 상세 조회 컨트롤러
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDetailResponse> readStudentDetail(@PathVariable(value = "studentId") final Long studentId) {

        final StudentDetailResponse response = studentService.readStudentDetail(studentId);

        return ResponseEntity.ok()
                .body(response);

    }

    // 오늘의 학생 일정 조회 컨트롤러
    @GetMapping("/today/{studentId}")
    public ResponseEntity<TodayScheduleResponse> readTodaySchedule(@PathVariable(value = "studentId") final Long studentId) {
        log.info("studentId: {}", studentId);

        final TodayScheduleResponse response = studentService.readTodaySchedule(studentId);

        return ResponseEntity.ok()
                .body(response);
    }

    // 날짜별 학생 일정 조회 컨트롤러
    @GetMapping("/date/{studentId}")
    public ResponseEntity<TodayScheduleResponse> readDateSchedule(@PathVariable(value = "studentId") final Long studentId, @RequestParam(value = "targetDate") final LocalDateTime targetDate) {
        log.info("studentId: {}", studentId);

        final TodayScheduleResponse response = studentService.readSchedule(studentId, targetDate);

        return ResponseEntity.ok()
                .body(response);
    }

    // 반 - 학생 정보 조회 컨트롤러
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<StudentAllResponse> readStudentBySection(@PathVariable(value = "sectionId") final Long sectionId) {

        final StudentAllResponse response = studentService.getAllStudentsBySection(sectionId);

        return ResponseEntity.ok()
                .body(response);

    }

    // 학생 수정 컨트롤러
    @PatchMapping("")
    public ResponseEntity<StudentResponse> updateStudent(@RequestBody final StudentUpdateRequest request) {

        final StudentResponse response = studentService.updateStudent(request);

        return ResponseEntity.ok()
                .body(response);
    }

    // 학생 삭제 컨트롤러
    @DeleteMapping("/{studentId}")
    public ResponseEntity<StudentResponse> deleteStudent(@PathVariable(value = "studentId") final Long studentId) {

        final StudentResponse response = studentService.deleteStudent(studentId);

        return ResponseEntity.ok()
                .body(response);
    }

}
