package com.yangyoung.server.student.service;

import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureResponse;
import com.yangyoung.server.lecture.service.LectureService;
import com.yangyoung.server.lecture.service.LectureSubService;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.student.dto.request.StudentCreationRequest;
import com.yangyoung.server.student.dto.request.StudentUpdateRequest;
import com.yangyoung.server.student.dto.response.*;
import com.yangyoung.server.studentTask.dto.response.StudentTaskAllResponse;
import com.yangyoung.server.task.dto.response.TaskAllResponse;
import com.yangyoung.server.task.service.TaskService;
import com.yangyoung.server.util.UtilService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final TaskService taskService;
    private final LectureService lectureService;
    private final StudentSubService studentSubService;
    private final SectionSubService sectionSubService;
    private final LectureSubService lectureSubService;
    private final UtilService utilService;

    // 학생 추가(인적 사항 기입 + 반 할당)
    @Transactional
    public StudentResponse createStudent(StudentCreationRequest request) {

        Student student = studentSubService.createStudentInfo(request.getId(), request.getName(), request.getSchool(),
                request.getGrade(), request.getStudentPhoneNumber(), request.getParentPhoneNumber());

        studentSubService.assignStudentToSection(student, request.getSectionId());

        return new StudentResponse(student);
    }

    // 학생 전체 조회(인적 사항 + 검색 옵션)
    @Transactional
    public StudentAllResponse readAllStudents() {

        List<Student> studentList = studentRepository.findAll();
        List<StudentResponse> studentResponseList = studentSubService.readAllStudentInfo(studentList);
        SearchOptionResponse searchOptionResponse = utilService.getSearchOptionList();

        return new StudentAllResponse(studentResponseList, searchOptionResponse.getSectionList(),
                searchOptionResponse.getGradeList(), searchOptionResponse.getSchoolList(), studentResponseList.size());
    }

    // 학생 상세 조회(인적 사항 + 수강 강의 + 과제)
    @Transactional
    public StudentDetailResponse readStudentDetail(Long studentId) {

        StudentResponse studentResponse = studentSubService.readStudentInfo(studentId);
        LectureAllResponse lectureAllResponse = lectureService.getAllLecturesBySection(studentResponse.getSectionId());
        StudentTaskAllResponse studentTaskAllResponse = taskService.readTaskByStudent(studentId);

        return new StudentDetailResponse(studentResponse, lectureAllResponse, studentTaskAllResponse);
    }

    // 학생 오늘 일정 조회(인적 사항 + 오늘 수강 강의 + 과제)
    @Transactional
    public TodayScheduleResponse readTodaySchedule(Long studentId) {

        log.info("학생 오늘 일정 조회");
        LocalDateTime now = LocalDateTime.now().plusHours(9);
        LocalDate today = now.toLocalDate();
        log.info("today : " + today);
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();
        StudentDetailResponse studentDetailResponse = readStudentDetail(studentId);

        List<LectureResponse> lectureResponseList = lectureSubService.getTodayLectures(studentDetailResponse.getLectureAllResponse(), todayDayOfWeek);
        LectureAllResponse lectureAllResponse = new LectureAllResponse(lectureResponseList, lectureResponseList.size());

        return new TodayScheduleResponse(
                today,
                studentDetailResponse.getStudentResponse(),
                lectureAllResponse,
                studentDetailResponse.getStudentTaskAllResponse());
    }

    // 반 - 학생 정보 조회
    @Transactional
    public StudentAllResponse getAllStudentsBySection(Long sectionId) {

        List<Student> studentList = studentRepository.findBySectionId(sectionId);
        List<StudentResponse> studentResponseList = studentSubService.readAllStudentInfo(studentList);

        return new StudentAllResponse(studentResponseList,
                null, null, null, studentResponseList.size());
    }

    // 학생 정보 수정
    @Transactional
    public StudentResponse updateStudent(final StudentUpdateRequest request) {

        Student student = studentSubService.isStudentExist(request.getStudentId());
        Section section = sectionSubService.isSectionExist(request.getSectionId());

        student.update(
                request.getSchool(),
                request.getGrade(),
                request.getStudentPhoneNumber(),
                request.getParentPhoneNumber(),
                section
        );
        studentRepository.save(student);

        return new StudentResponse(student);
    }


    // 학생 삭제
    @Transactional
    public StudentResponse deleteStudent(Long studentId) {

        Student student = studentSubService.isStudentExist(studentId);
        studentRepository.delete(student);

        return new StudentResponse(student);
    }
}
