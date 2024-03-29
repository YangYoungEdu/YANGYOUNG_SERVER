package com.yangyoung.server.task.controller;

import com.yangyoung.server.sectionTask.dto.response.SectionTaskAllResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskAllResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskResponse;
import com.yangyoung.server.task.dto.request.*;
import com.yangyoung.server.task.dto.response.TaskPostResponse;
import com.yangyoung.server.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/task")
public class TaskController {

    private final TaskService taskService;

    // 과제 여러분반 일괄 추가
    @PostMapping("/sections")
    public ResponseEntity<Void> createTaskBySections(@RequestBody TaskMultipleSectionRequest request) {

        taskService.createTaskBySections(request);

        return ResponseEntity.ok()
                .build();
    }

    /* 개인 과제 추가 */
    @PostMapping("/student")
    public ResponseEntity<TaskPostResponse> postTaskByStudent(@RequestBody TaskStudentRequest request) {

        TaskPostResponse response = taskService.createTaskByStudent(request);

        return ResponseEntity.ok()
                .body(response);

    }

    /* 반별 과제 추가 */
    @PostMapping("/section")
    public ResponseEntity<TaskPostResponse> postTaskBySection(@RequestBody TaskSectionRequest request) {

        TaskPostResponse response = taskService.createTaskBySection(request);

        return ResponseEntity.ok()
                .body(response);
    }

//    /* 개인 과제 조회 */
//    @GetMapping("/student/{studentId}")
//    public ResponseEntity<StudentTaskAllResponse> getTaskByStudent(@PathVariable Long studentId) {
//
//        StudentTaskAllResponse response = taskService.getTaskByStudent(studentId);
//
//        return ResponseEntity.ok()
//                .body(response);
//    }
//
//    /* 반별 과제 조회 */
//    @GetMapping("/section/{sectionId}")
//    public ResponseEntity<SectionTaskAllResponse> getTaskBySection(@PathVariable Long sectionId) {
//
//        SectionTaskAllResponse response = taskService.readTaskBySection(sectionId);
//
//        return ResponseEntity.ok()
//                .body(response);
//    }

    /* 과제 상태 수정 */
    @PatchMapping("/progress")
    public ResponseEntity<StudentTaskAllResponse> putTask(@RequestBody List<TaskProgressUpdateRequest> requestList) {

        StudentTaskAllResponse response = taskService.updateTaskProgress(requestList);

        return ResponseEntity.ok()
                .body(response);
    }

    // 과제 정보 수정
    @PatchMapping("")
    public ResponseEntity<Void> putTask(@RequestBody TaskUpdateRequest request) {

        taskService.updateTask(request);

        return ResponseEntity.noContent().build();
    }

    // 과제 삭제
    @DeleteMapping("")
    public ResponseEntity<Void> deleteTask(@RequestParam List<Long> taskIdList) {

        taskService.deleteTask(taskIdList);

        return ResponseEntity.noContent().build();
    }

    // 날짜별 과제 조회 - 학생
    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentTaskAllResponse> getTaskByStudentAndDate(@PathVariable Long studentId, @RequestParam LocalDate date) {

        StudentTaskAllResponse response = taskService.getTasksByStudentAndDate(studentId, date);

        return ResponseEntity.ok()
                .body(response);
    }

    // 날짜별 과제 조회 - 반
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<SectionTaskAllResponse> getTaskBySectionAndDate(@PathVariable Long sectionId, @RequestParam LocalDate date) {

        SectionTaskAllResponse response = taskService.getTasksBySectionAndDate(sectionId, date);

        return ResponseEntity.ok()
                .body(response);
    }
}