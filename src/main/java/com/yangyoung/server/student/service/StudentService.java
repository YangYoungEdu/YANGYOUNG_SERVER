package com.yangyoung.server.student.service;

import com.yangyoung.server.exception.student.StudentIdDuplicatedException;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureBriefResponse;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.dto.response.SectionAllBriefResponse;
import com.yangyoung.server.section.dto.response.SectionBriefResponse;
import com.yangyoung.server.section.service.SectionSubService;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentSectionRepository studentSectionRepository;
    private final SectionLectureRepository sectionLectureRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final SectionRepository sectionRepository;

    private final StudentSubService studentSubService;
    private final SectionSubService sectionSubService;

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    // 학생 추가(인적 사항 기입 + 반 할당)
    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {

        isStudentIdDuplicate(request.getId());

        Student student = studentRepository.save(request.toEntity());

        assignStudentToSections(student, request.getSectionIdList());

        return new StudentResponse(student);
    }

    // ID 중복 검사
    private void isStudentIdDuplicate(Long studentId) {

        boolean isStudentIdExist = studentRepository.existsById(studentId);
        if (isStudentIdExist) {
            String message = String.format("Student Id is already exist. (studentId: %d)", studentId);
            logger.info(message);
            throw new StudentIdDuplicatedException(message);
        }
    }

    // 학생 -> 반 할당
    private void assignStudentToSections(Student student, List<Long> sectionIdList) {

        List<Section> sectionList = sectionRepository.findAllById(sectionIdList);

        List<StudentSection> studentSectionList = sectionList.stream()
                .map(section -> new StudentSection(student, section))
                .toList();
        studentSectionRepository.saveAll(studentSectionList);
    }

    // 학생 전체 조회
    @Transactional
    public StudentAllResponse getAllStudents() {

        List<Student> studentList = studentRepository.findAll();
        List<StudentResponse> studentResponseList = getStudentResponseList(studentList);

        return new StudentAllResponse(
                studentResponseList,
                studentResponseList.size());
    }

    // 반에 속한 학생 전체 조회
    @Transactional
    public StudentAllResponse getAllStudentsBySection(Long sectionId) {

        List<StudentSection> studentSectionList = studentSectionRepository.findAllBySectionId(sectionId);
        List<Student> studentList = studentSectionList.stream()
                .map(StudentSection::getStudent)
                .toList();
        List<StudentResponse> studentResponseList = getStudentResponseList(studentList);

        return new StudentAllResponse(
                studentResponseList,
                studentResponseList.size());
    }

    // Student 리스트 -> StudentBriefResponse 리스트 변환
    private List<StudentResponse> getStudentResponseList(List<Student> studentList) {
        return studentList.stream()
                .map(StudentResponse::new)
                .toList();
    }

    // 학생 상세 조회(인적 사항 + 수강 강의 + 과제)
    @Transactional
    public StudentDetailResponse getStudentDetail(Long studentId) {

        StudentResponse studentResponse = new StudentResponse(studentSubService.findStudentByStudentId(studentId));
        SectionAllBriefResponse sectionAllBriefResponse = findSectionsBriefInfoByStudentId(studentId);
        LectureAllResponse lectureAllResponse = getLecturesByStudentId(studentId);

        return new StudentDetailResponse(
                studentResponse,
                sectionAllBriefResponse,
                lectureAllResponse);
    }

    // 학생이 속한 반 정보 조회
    private SectionAllBriefResponse findSectionsBriefInfoByStudentId(Long studentId) {

        List<StudentSection> studentSectionList = studentSectionRepository.findByStudentId(studentId);
        List<SectionBriefResponse> sectionBriefResponseList = studentSectionList.stream()
                .map(studentSection -> new SectionBriefResponse(studentSection.getSection()))
                .toList();

        return new SectionAllBriefResponse(sectionBriefResponseList, sectionBriefResponseList.size());
    }

    // 학생별 강의 조회
    private LectureAllResponse getLecturesByStudentId(Long studentId) {

        List<Section> sectionList = sectionSubService.findSectionsByStudentId(studentId);
        List<Long> sectionIdList = sectionList.stream()
                .map(Section::getId)
                .toList();

        List<SectionLecture> sectionLectureList = sectionLectureRepository.findAllBySectionIdIn(sectionIdList);
        List<LectureBriefResponse> lectureBriefResponseList = sectionLectureList.stream()
                .map(sectionLecture -> new LectureBriefResponse(sectionLecture.getLecture()))
                .toList();

        return new LectureAllResponse(lectureBriefResponseList, lectureBriefResponseList.size());
    }

    // 학생 오늘 일정 조회(인적 사항 + 오늘 수강 강의 + 과제)
    @Transactional
    public TodayScheduleResponse readTodaySchedule(Long studentId) {

        LocalDateTime now = LocalDateTime.now().plusHours(9);
        LocalDate today = now.toLocalDate();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();

        StudentResponse studentResponse = new StudentResponse(studentSubService.findStudentByStudentId(studentId));
        LectureAllResponse lectureAllResponse = findTodayLecturesByStudentId(studentId, todayDayOfWeek, today);
        StudentTaskAllResponse studentTaskAllResponse = findTodayTodayTasksByStudentId(studentId, today);
        String homeRoom = findHomeRoomByStudentId(studentId);

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
        List<StudentTaskResponse> studentTaskResponseList = studentTaskRepository.findByStudentId(studentId).stream()
                .filter(studentTask -> studentTask.getTask().getTaskDate().equals(today))
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

        Student student = studentSubService.findStudentByStudentId(request.getStudentId());
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

        Student student = studentSubService.findStudentByStudentId(studentId);
        studentRepository.delete(student);

        List<StudentSection> studentSectionList = studentSectionRepository.findByStudentId(studentId);
        studentSectionRepository.deleteAll(studentSectionList);

        return new StudentResponse(student);
    }
}
