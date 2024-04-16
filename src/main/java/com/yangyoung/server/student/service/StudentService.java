package com.yangyoung.server.student.service;

import com.yangyoung.server.exception.student.StudentIdDuplicatedException;
import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureBriefResponse;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.dto.response.SectionAllBriefResponse;
import com.yangyoung.server.section.dto.response.SectionBriefResponse;
import com.yangyoung.server.section.service.SectionUtilService;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import com.yangyoung.server.sectionLecture.domain.SectionLectureRepository;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.student.dto.request.StudentCreateRequest;
import com.yangyoung.server.student.dto.request.StudentUpdateRequest;
import com.yangyoung.server.student.dto.response.StudentAllResponse;
import com.yangyoung.server.student.dto.response.StudentDetailResponse;
import com.yangyoung.server.student.dto.response.StudentResponse;
import com.yangyoung.server.student.dto.response.TodayScheduleResponse;
import com.yangyoung.server.studentSection.domain.StudentSection;
import com.yangyoung.server.studentSection.domain.StudentSectionRepository;
import com.yangyoung.server.studentTask.domain.StudentTask;
import com.yangyoung.server.studentTask.domain.StudentTaskRepository;
import com.yangyoung.server.studentTask.dto.response.StudentTaskAllResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentSectionRepository studentSectionRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final SectionLectureRepository sectionLectureRepository;

    private final StudentUtilService studentUtilService;
    private final SectionUtilService sectionUtilService;

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    // 학생 추가
    @Transactional
    public StudentResponse addStudent(StudentCreateRequest request) {

        checkStudentIdDuplicate(request.getId()); // ID 중복 검사
        Student newStudent = studentRepository.save(request.toEntity()); // 학생 정보 저장
        assignStudentToSections(newStudent, request.getSectionIdList()); // 학생 -> 반 할당

        return new StudentResponse(newStudent);
    }

    // 학생 ID 중복 검사
    private void checkStudentIdDuplicate(Long studentId) {

        boolean isStudentIdExist = studentRepository.existsById(studentId);
        if (isStudentIdExist) { // ID 중복 시 예외 발생
            String message = String.format("Student Id is already exist. (studentId: %d)", studentId);
            logger.info(message);
            throw new StudentIdDuplicatedException(message);
        }
    }

    // 학생 -> 반 할당
    private void assignStudentToSections(Student student, List<Long> sectionIdList) {

        List<Section> sectionList = sectionUtilService.findSectionsBySectionIdList(sectionIdList);
        if (sectionList.isEmpty()) { // 반 정보가 없는 경우
            return;
        }

        List<StudentSection> studentSectionList = sectionList.stream()
                .map(section -> new StudentSection(student, section))
                .toList();
        studentSectionRepository.saveAll(studentSectionList);
    }

    // 학생 전체 조회
    @Transactional
    public StudentAllResponse getAllStudents() {

        List<Student> studentList = studentRepository.findAll();
        if (studentList.isEmpty()) { // 학생 정보가 없는 경우
            return new StudentAllResponse(Collections.emptyList(), 0);
        }

        List<StudentResponse> studentResponseList = getStudentResponseList(studentList);

        return new StudentAllResponse(
                studentResponseList,
                studentResponseList.size());
    }

    // 반에 속한 학생 전체 조회
    @Transactional
    public StudentAllResponse getAllStudentsBySection(Long sectionId) {

        List<Student> studentList = studentSectionRepository.findStudentsBySectionId(sectionId);
        if (studentList.isEmpty()) { // 학생 정보가 없는 경우
            return new StudentAllResponse(Collections.emptyList(), 0);
        }

        List<StudentResponse> studentResponseList = getStudentResponseList(studentList);

        return new StudentAllResponse(
                studentResponseList,
                studentResponseList.size());
    }

    // Student 리스트 -> StudentResponse 리스트 변환
    private List<StudentResponse> getStudentResponseList(List<Student> studentList) {
        return studentList.stream()
                .map(StudentResponse::new)
                .toList();
    }

    // 학생 상세 조회(인적 사항 + 수강 강의 + 과제)
    @Transactional
    public StudentDetailResponse getStudentDetail(Long studentId) {

        StudentResponse studentResponse = getStudentInfo(studentId); // 학생 정보 조회
        SectionAllBriefResponse sectionAllBriefResponse = getSectionsBriefInfoByStudentId(studentId); // 학생이 속한 반 정보 조회
        LectureAllResponse lectureAllResponse = getLecturesByStudentId(studentId); // 학생별 강의 조회

        return new StudentDetailResponse(
                studentResponse,
                sectionAllBriefResponse,
                lectureAllResponse);
    }

    // 인적 사항 조회
    private StudentResponse getStudentInfo(Long studentId) {
        return new StudentResponse(studentUtilService.findStudentByStudentId(studentId));
    }

    // 학생이 속한 반 정보 조회
    private SectionAllBriefResponse getSectionsBriefInfoByStudentId(Long studentId) {

        List<Section> sectionList = studentSectionRepository.findSectionByStudentId(studentId);
        if (sectionList.isEmpty()) { // 학생이 속한 반 정보가 없는 경우
            return new SectionAllBriefResponse(Collections.emptyList(), 0);
        }

        List<SectionBriefResponse> sectionBriefResponseList = sectionList.stream()
                .map(SectionBriefResponse::new)
                .toList();

        return new SectionAllBriefResponse(sectionBriefResponseList, sectionBriefResponseList.size());
    }

    // 학생별 강의 조회 -- 수정 필요
    private LectureAllResponse getLecturesByStudentId(Long studentId) {

        List<Long> sectionIdList = studentSectionRepository.findSectionByStudentId(studentId).stream()
                .map(Section::getId)
                .toList();

        List<Lecture> lectureList = sectionLectureRepository.findLectureBySectionIdIn(sectionIdList);
        List<LectureBriefResponse> lectureBriefResponseList = lectureList.stream()
                .map(LectureBriefResponse::new)
                .toList();

        return new LectureAllResponse(lectureBriefResponseList, lectureBriefResponseList.size());
    }

    // 학생 오늘 일정 조회
    @Transactional
    public TodayScheduleResponse getTodaySchedule(Long studentId) {

        LocalDateTime now = LocalDateTime.now().plusHours(9);
        LocalDate today = now.toLocalDate();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();

        StudentResponse studentResponse = getStudentInfo(studentId); // 학생 정보 조회
        LectureAllResponse lectureAllResponse = findTodayLecturesByStudentId(studentId, todayDayOfWeek, today); // 학생 오늘 강의 조회
        StudentTaskAllResponse studentTaskAllResponse = findTodayTodayTasksByStudentId(studentId, today); // 학생 오늘 과제 조회
        String homeRoom = findHomeRoomByStudentId(studentId); // 학생 홈룸 조회

        return new TodayScheduleResponse(
                today,
                studentResponse,
                lectureAllResponse,
                studentTaskAllResponse,
                homeRoom);
    }

    // 학생 오늘 강의 조회
    private LectureAllResponse findTodayLecturesByStudentId(Long studentId, DayOfWeek todayDayOfWeek, LocalDate today) {
        LectureAllResponse lectureAllResponse = getLecturesByStudentId(studentId);
        List<LectureBriefResponse> lectureBriefResponseList = lectureAllResponse.getLectureResponseList().stream()
                .filter(lecture -> lecture.getDayList().contains(todayDayOfWeek.toString()) || lecture.getDateList().contains(today))
                .sorted()
                .toList();
        return new LectureAllResponse(lectureBriefResponseList, lectureBriefResponseList.size());
    }

    // 학생 오늘 과제 조회
    private StudentTaskAllResponse findTodayTodayTasksByStudentId(Long studentId, LocalDate today) {

        List<StudentTask> studentTaskList = studentTaskRepository.findByStudentIdAndTask_TaskDate(studentId, today);
        List<StudentTaskResponse> studentTaskResponseList = studentTaskList.stream()
                .map(studentTask -> new StudentTaskResponse(
                        studentTask.getId(),
                        studentTask.getTask().getContent(),
                        studentTask.getTask().getTaskDate(),
                        studentTask.getTaskProgress().getProgressName(),
                        studentTask.getTask().getTaskType().getTypeName()))
                .toList();

        return new StudentTaskAllResponse(studentTaskResponseList, studentTaskResponseList.size());
    }

    // 학생 홈룸 조회
    private String findHomeRoomByStudentId(Long studentId) {
        List<StudentSection> studentSectionList = studentSectionRepository.findByStudentId(studentId);
        return studentSectionList.get(0).getSection().getHomeRoom();
    }

    // 학생 정보 수정(인적 사항 + 반정보)
    @Transactional
    public StudentResponse updateStudent(StudentUpdateRequest request) {

        Student student = studentUtilService.findStudentByStudentId(request.getStudentId());
        student.update(
                request.getSchool(),
                request.getGrade(),
                request.getStudentPhoneNumber(),
                request.getParentPhoneNumber()
        );

        return new StudentResponse(student);
    }

    // 학생 삭제 -- 수정 필요
    @Transactional
    public StudentResponse deleteStudent(Long studentId) {

        Student student = studentUtilService.findStudentByStudentId(studentId);
        studentRepository.delete(student);

        List<StudentSection> studentSectionList = studentSectionRepository.findByStudentId(studentId);
        studentSectionRepository.deleteAll(studentSectionList);

        return new StudentResponse(student);
    }
}
