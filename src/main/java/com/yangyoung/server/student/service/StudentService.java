package com.yangyoung.server.student.service;

import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureResponse;
import com.yangyoung.server.lecture.service.LectureSubService;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.dto.response.SectionAllBriefResponse;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.student.dto.request.StudentCreateRequest;
import com.yangyoung.server.student.dto.request.StudentUpdateRequest;
import com.yangyoung.server.student.dto.response.*;
import com.yangyoung.server.studentSection.domain.StudentSection;
import com.yangyoung.server.studentSection.domain.StudentSectionRepository;
import com.yangyoung.server.studentTask.dto.response.StudentTaskAllResponse;
import com.yangyoung.server.task.service.TaskService;
import com.yangyoung.server.task.service.TaskSubService;
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
    private final StudentSectionRepository studentSectionRepository;

    private final StudentSubService studentSubService;
    private final SectionSubService sectionSubService;
    private final LectureSubService lectureSubService;
    private final TaskService taskService;
    private final UtilService utilService;

    // 학생 추가(인적 사항 기입 + 반 할당)
    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {

        Student student = studentSubService.createStudentInfo(request.getId(), request.getName(), request.getSchool(),
                request.getGrade(), request.getStudentPhoneNumber(), request.getParentPhoneNumber());

        studentSubService.assignStudentToSection(student, request.getSectionIdList());

        List<String> sectionNameList = sectionSubService.findSectionNamesByStudentId(student.getId());

        return new StudentResponse(student, sectionNameList);
    }

    // 학생 전체 조회(인적 사항 + 검색 옵션)
    @Transactional
    public StudentAllResponse readAllStudents() {

        List<Student> studentList = studentRepository.findAll();
        List<StudentResponse> studentResponseList = studentSubService.readAllStudentInfo(studentList);

        SearchOptionResponse searchOptionResponse = utilService.getSearchOptionList();

        return new StudentAllResponse(
                studentResponseList,
                searchOptionResponse.getGradeList(),
                searchOptionResponse.getSchoolList(),
                studentResponseList.size());
    }

    // 학생 상세 조회(인적 사항 + 수강 강의 + 과제)
    @Transactional
    public StudentDetailResponse readStudentDetail(Long studentId) {

        StudentResponse studentResponse = studentSubService.getStudentInfo(studentId);
        SectionAllBriefResponse sectionAllBriefResponse = sectionSubService.findSectionsBriefInfo(studentId);
        LectureAllResponse lectureAllResponse = lectureSubService.getLecturesByStudent(studentId);

        return new StudentDetailResponse(
                studentResponse,
                sectionAllBriefResponse,
                lectureAllResponse);
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

        StudentTaskAllResponse studentTaskAllResponse = taskService.getTasksByStudentAndDate(studentId, today);

        String homeRoom = sectionSubService.findSectionHomeRoomsByStudentId(studentId).get(0);

        return new TodayScheduleResponse(
                today,
                studentDetailResponse.getStudentResponse(),
                lectureAllResponse,
                studentTaskAllResponse,
                homeRoom);
    }

    // 반 - 학생 정보 조회
    @Transactional
    public StudentAllResponse getAllStudentsBySection(Long sectionId) {

        List<StudentSection> studentSectionList = studentSectionRepository.findAllBySectionId(sectionId);
        List<Student> studentList = studentSectionList.stream()
                .map(StudentSection::getStudent)
                .toList();
        List<StudentResponse> studentResponseList = studentSubService.readAllStudentInfo(studentList);

        return new StudentAllResponse(
                studentResponseList,
                null,
                null,
                studentResponseList.size());
    }

    // 학생 정보 수정(인적 사항 + 반정보)
    @Transactional
    public StudentResponse updateStudent(final StudentUpdateRequest request) {

        Student student = studentSubService.findStudentByStudentId(request.getStudentId());
        student.update(
                request.getSchool(),
                request.getGrade(),
                request.getStudentPhoneNumber(),
                request.getParentPhoneNumber()
        );
        studentRepository.save(student);

        List<String> sectionNameList = sectionSubService.findSectionNamesByStudentId(request.getStudentId());

        return new StudentResponse(student, sectionNameList);
    }


    // 학생 삭제
    @Transactional
    public StudentResponse deleteStudent(Long studentId) {

        Student student = studentSubService.findStudentByStudentId(studentId);
        studentRepository.delete(student);

        List<StudentSection> studentSectionList = studentSectionRepository.findAllByStudentId(studentId);
        studentSectionRepository.deleteAll(studentSectionList);

        List<String> sectionNameList = sectionSubService.findSectionNamesByStudentId(studentId);

        return new StudentResponse(student, sectionNameList);
    }
}
