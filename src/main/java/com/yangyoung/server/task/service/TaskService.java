package com.yangyoung.server.task.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.sectionTask.domain.SectionTask;
import com.yangyoung.server.sectionTask.domain.SectionTaskRepository;
import com.yangyoung.server.sectionTask.dto.request.SectionTaskRequest;
import com.yangyoung.server.sectionTask.dto.response.SectionTaskAllResponse;
import com.yangyoung.server.sectionTask.dto.response.SectionTaskResponse;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.student.service.StudentSubService;
import com.yangyoung.server.studentTask.domain.StudentTask;
import com.yangyoung.server.studentTask.domain.StudentTaskRepository;
import com.yangyoung.server.studentTask.domain.TaskProgress;
import com.yangyoung.server.studentTask.dto.response.StudentTaskAllResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskResponse;
import com.yangyoung.server.task.domain.Task;
import com.yangyoung.server.task.domain.TaskRepository;
import com.yangyoung.server.task.dto.request.TaskSectionRequest;
import com.yangyoung.server.task.dto.request.TaskStudentRequest;
import com.yangyoung.server.task.dto.request.TaskUpdateRequest;
import com.yangyoung.server.task.dto.response.TaskAllResponse;
import com.yangyoung.server.task.dto.response.TaskPostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final StudentRepository studentRepository;
    private final StudentSubService studentSubService;
    private final SectionSubService sectionSubService;
    private final SectionTaskRepository sectionTaskRepository;

    // 개인 과제 추가
    @Transactional
    public TaskPostResponse createTaskByStudent(TaskStudentRequest request) {

        Task task = taskRepository.save(request.toEntity());

        Student student = studentSubService.isStudentExist(request.getStudentId());

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
    public StudentTaskAllResponse readTaskByStudent(Long studentId) {

        List<StudentTask> studentTaskList = studentTaskRepository.findByStudentId(studentId);
        List<StudentTaskResponse> taskResponseList = studentTaskList.stream()
                .map(studentTask -> new StudentTaskResponse(studentTask.getId(), studentTask.getTask().getAssignment(), studentTask.getTaskProgress().getProgressName()))
                .toList();

        return new StudentTaskAllResponse(taskResponseList, taskResponseList.size());
    }

    // 반별 과제 추가
    @Transactional
    public TaskPostResponse createTaskBySection(TaskSectionRequest request) {

        Section section = sectionSubService.isSectionExist(request.getSectionId());

        Task task = taskRepository.save(request.toEntity());

        SectionTask sectionTask = SectionTask.builder().
                section(section).
                task(task).
                taskProgress(TaskProgress.NOT_STARTED).
                build();
        sectionTaskRepository.save(sectionTask);

        List<Student> students = studentRepository.findBySectionId(section.getId());
        List<StudentTask> studentTaskList = new ArrayList<>();
        for (Student student : students) {
            StudentTask studentTask = new StudentTask(student, task, TaskProgress.NOT_STARTED);
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
            SectionTaskResponse taskResponse = new SectionTaskResponse(task.getId(), task.getAssignment(), section.getName());
            taskList.add(taskResponse);
        }

        return new SectionTaskAllResponse(taskList, taskList.size());
    }

    /* 과제 상태 변경 */
    @Transactional
    public StudentTaskResponse updateTaskProgress(TaskUpdateRequest request) {

        StudentTask studentTask = studentTaskRepository.findByStudentIdAndTaskId(request.getStudentId(), request.getTaskId());
        if (studentTask == null) { /* id에 해당하는 학생과 과제가 존재하지 않는 경우 */
            throw new MyException(ErrorCode.STUDENT_TASK_NOT_FOUND);
        }

        log.info(request.getTaskProgress().toString());
        studentTask.updateTaskProgress(request.getTaskProgress());
        studentTaskRepository.save(studentTask);

        return new StudentTaskResponse(studentTask.getId(), studentTask.getTask().getAssignment(), studentTask.getTaskProgress().getProgressName());
    }
}
