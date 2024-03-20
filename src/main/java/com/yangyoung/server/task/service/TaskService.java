package com.yangyoung.server.task.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.sectionTask.domain.SectionTask;
import com.yangyoung.server.sectionTask.domain.SectionTaskRepository;
import com.yangyoung.server.sectionTask.dto.response.SectionTaskAllResponse;
import com.yangyoung.server.sectionTask.dto.response.SectionTaskResponse;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.service.StudentSubService;
import com.yangyoung.server.studentTask.domain.StudentTask;
import com.yangyoung.server.studentTask.domain.StudentTaskRepository;
import com.yangyoung.server.studentTask.domain.TaskProgress;
import com.yangyoung.server.studentTask.dto.response.StudentTaskAllResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskResponse;
import com.yangyoung.server.task.domain.Task;
import com.yangyoung.server.task.domain.TaskRepository;
import com.yangyoung.server.task.domain.TaskType;
import com.yangyoung.server.task.dto.request.TaskSectionRequest;
import com.yangyoung.server.task.dto.request.TaskStudentRequest;
import com.yangyoung.server.task.dto.request.TaskProgressUpdateRequest;
import com.yangyoung.server.task.dto.request.TaskUpdateRequest;
import com.yangyoung.server.task.dto.response.TaskPostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskType STUDENT = TaskType.STUDENT;
    private final TaskType SECTION = TaskType.SECTION;

    private final TaskRepository taskRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final SectionTaskRepository sectionTaskRepository;

    private final StudentSubService studentSubService;
    private final SectionSubService sectionSubService;

    // 개인 과제 추가
    @Transactional
    public TaskPostResponse createTaskByStudent(TaskStudentRequest request) {

        Task task = taskRepository.save(request.toEntity());

        Student student = studentSubService.findStudentByStudentId(request.getStudentId());

        StudentTask studentTask = StudentTask.builder().
                student(student).
                task(task).
                taskProgress(TaskProgress.NOT_STARTED).
                build();
        studentTaskRepository.save(studentTask);

        return new TaskPostResponse(task, student);
    }

    // 개인 과제 조회
    @Transactional
    public StudentTaskAllResponse getTaskByStudent(Long studentId) {

        List<StudentTask> studentTaskList = studentTaskRepository.findByStudentId(studentId);
        List<StudentTaskResponse> taskResponseList = studentTaskList.stream()
                .map(studentTask -> new StudentTaskResponse(
                        studentTask.getId(),
                        studentTask.getTask().getContent(),
                        studentTask.getTask().getTaskDate(),
                        studentTask.getTaskProgress().getProgressName()))
                .toList();

        return new StudentTaskAllResponse(taskResponseList, taskResponseList.size());
    }

    // 반별 과제 추가
    @Transactional
    public TaskPostResponse createTaskBySection(TaskSectionRequest request) {

        Section section = sectionSubService.findSectionBySectionId(request.getSectionId());

        Task task = taskRepository.save(request.toEntity());

        SectionTask sectionTask = SectionTask.builder().
                section(section).
                task(task).
                taskType(SECTION).
                build();
        sectionTaskRepository.save(sectionTask);


        List<Student> studentList = studentSubService.getStudentsBySectionId(request.getSectionId());
        List<StudentTask> studentTaskList = new ArrayList<>();
        for (Student student : studentList) {
            StudentTask studentTask = new StudentTask(
                    student, task, TaskProgress.NOT_STARTED, STUDENT);
            studentTaskList.add(studentTask);
        }
        studentTaskRepository.saveAll(studentTaskList);

        return new TaskPostResponse(task, section);
    }


    // 반별 과제 조회
    @Transactional
    public SectionTaskAllResponse readTaskBySection(Long sectionId) {

        List<SectionTask> sectionTaskList = sectionTaskRepository.findBySectionId(sectionId);
        List<SectionTaskResponse> taskList = new ArrayList<>();
        for (SectionTask sectionTask : sectionTaskList) {
            Task task = sectionTask.getTask();
            Section section = sectionTask.getSection();
            SectionTaskResponse taskResponse = new SectionTaskResponse(task.getId(), task.getContent(), section.getName());
            taskList.add(taskResponse);
        }

        return new SectionTaskAllResponse(taskList, taskList.size());
    }

    /* 과제 상태 변경 */
    @Transactional
    public StudentTaskResponse updateTaskProgress(TaskProgressUpdateRequest request) {

        StudentTask studentTask = studentTaskRepository.findByStudentIdAndTaskId(request.getStudentId(), request.getTaskId());
        if (studentTask == null) { /* id에 해당하는 학생과 과제가 존재하지 않는 경우 */
            throw new MyException(ErrorCode.STUDENT_TASK_NOT_FOUND);
        }

        log.info(request.getTaskProgress().toString());
        studentTask.updateTaskProgress(request.getTaskProgress());
        studentTaskRepository.save(studentTask);

        return new StudentTaskResponse(
                studentTask.getId(),
                studentTask.getTask().getContent(),
                studentTask.getTask().getTaskDate(),
                studentTask.getTaskProgress().getProgressName());
    }

    // task 수정
    @Transactional
    public void updateTask(TaskUpdateRequest request) {

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new MyException(ErrorCode.TASK_NOT_FOUND));

        task.updateTask(request.getContent(), request.getTaskDate());
        taskRepository.save(task);
    }

    // task 삭제
    @Transactional
    public void deleteTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new MyException(ErrorCode.TASK_NOT_FOUND));
        taskRepository.delete(task);
    }

    // 날짜별로 과제 조회 - 학생
    @Transactional
    public StudentTaskAllResponse getTasksByStudentAndDate(Long studentId, LocalDate taskDate) {

        List<StudentTask> studentTaskList = studentTaskRepository.findByStudentIdAndTask_TaskDate(studentId, taskDate);
        List<StudentTaskResponse> taskResponseList = studentTaskList.stream()
                .map(studentTask -> new StudentTaskResponse(
                        studentTask.getTask().getId(),
                        studentTask.getTask().getContent(),
                        studentTask.getTask().getTaskDate(),
                        studentTask.getTaskProgress().getProgressName()))
                .toList();

        return new StudentTaskAllResponse(taskResponseList, taskResponseList.size());
    }

    // 날짜별로 과제 조회 - 반
    @Transactional
    public SectionTaskAllResponse getTasksBySectionAndDate(Long sectionId, LocalDate taskDate) {

        List<SectionTask> sectionTaskList = sectionTaskRepository.findBySectionIdAndTask_TaskDate(sectionId, taskDate);
        List<SectionTaskResponse> taskResponseList = sectionTaskList.stream()
                .map(sectionTask -> new SectionTaskResponse(
                        sectionTask.getTask().getId(),
                        sectionTask.getTask().getContent(),
                        sectionTask.getSection().getName()))
                .toList();

        return new SectionTaskAllResponse(taskResponseList, taskResponseList.size());
    }
}
