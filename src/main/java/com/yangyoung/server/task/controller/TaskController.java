package com.yangyoung.server.task.controller;

import com.yangyoung.server.sectionTask.dto.response.SectionTaskAllResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskAllResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskResponse;
import com.yangyoung.server.task.dto.request.TaskSectionRequest;
import com.yangyoung.server.task.dto.request.TaskStudentRequest;
import com.yangyoung.server.task.dto.request.TaskProgressUpdateRequest;
import com.yangyoung.server.task.dto.request.TaskUpdateRequest;
import com.yangyoung.server.task.dto.response.TaskPostResponse;
import com.yangyoung.server.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/task")
public class TaskController {

    private final TaskService taskService;

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
    public ResponseEntity<StudentTaskResponse> putTask(@RequestBody TaskProgressUpdateRequest request) {

        StudentTaskResponse response = taskService.updateTaskProgress(request);

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
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {

        taskService.deleteTask(taskId);

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