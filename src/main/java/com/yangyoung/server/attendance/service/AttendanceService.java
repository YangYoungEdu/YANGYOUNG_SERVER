package com.yangyoung.server.attendance.service;

import com.yangyoung.server.attendance.domain.Attendance;
import com.yangyoung.server.attendance.domain.AttendanceRepository;
import com.yangyoung.server.attendance.domain.AttendanceType;
import com.yangyoung.server.attendance.dto.request.AttendanceStudentRequest;
import com.yangyoung.server.attendance.dto.request.AttendanceSectionRequest;
import com.yangyoung.server.attendance.dto.request.AttendanceUpdateRequest;
import com.yangyoung.server.attendance.dto.response.AttendanceAllResponse;
import com.yangyoung.server.attendance.dto.response.AttendanceResponse;
import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.student.service.StudentSubService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;

    private final StudentSubService studentSubService;
    private final SectionSubService sectionSubService;

    // 출석 체크 메소드 (학생)
    @Transactional
    public void attend(AttendanceStudentRequest request) {

//        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now = LocalDateTime.now().plusHours(9);
        LocalDateTime start = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(23).withMinute(59).withSecond(59);

        log.info("isEmpty");
        Optional<Student> attendedStudent = studentRepository.findById(request.getStudentId());
        if (attendedStudent.isEmpty()) { // 해당 학생이 존재하지 않을 때
            throw new MyException(ErrorCode.STUDENT_NOT_FOUND);
        }

//        Optional<Attendance> check = attendanceRepository.findByStudentIdAndAttendedDateTimeBetween(
//                request.getStudentId(), start, end);
//        if (check.isPresent()) {
//            check.get().updateAttendanceType(AttendanceType.ATTENDANCE);
//            attendanceRepository.save(check.get());
//        }

        List<Section> sectionList = sectionSubService.findSectionsByStudentId(request.getStudentId());
        List<Attendance> attendanceList = new ArrayList<>();
        for (Section section : sectionList) {
            Attendance attendance = new Attendance(
                    now,
                    AttendanceType.ATTENDANCE,
                    request.getNote(),
                    attendedStudent.get(),
                    section);
            attendanceList.add(attendance);
        }
        attendanceRepository.saveAll(attendanceList);
    }

    // 출석 체크 메소드 (반)
    @Transactional
    public AttendanceAllResponse attendBySection(AttendanceSectionRequest request) {

        LocalDate targetDate;
        log.info("list size: {}", request.getAttendanceUpdateRequestList().size());
        if (request.getAttendanceUpdateRequestList().isEmpty()) {
            targetDate = LocalDate.now();
        } else {
            targetDate = request.getAttendanceUpdateRequestList().get(0).getAttendedDateTime().plusHours(9).toLocalDate();
        }
//        LocalDate targetDate = request.getAttendanceUpdateRequestList().get(0).getAttendedDateTime().toLocalDate();
        LocalDateTime startDateTime = targetDate.atStartOfDay();
        LocalDateTime endDateTime = targetDate.atTime(23, 59, 59);

        Optional<Section> attendedSection = sectionRepository.findById(request.getSectionId());
        if (attendedSection.isEmpty()) { // 해당 섹션이 존재하지 않을 때
            throw new MyException(ErrorCode.SECTION_NOT_FOUND);
        }

        List<Attendance> attendanceList = new ArrayList<>();
        List<AttendanceUpdateRequest> attendanceStudentRequestedList = request.getAttendanceUpdateRequestList();
        for (AttendanceUpdateRequest attendanceUpdateRequest : attendanceStudentRequestedList) {

            AttendanceType attendanceType = attendanceUpdateRequest.getAttendanceType();
            String note = attendanceUpdateRequest.getNote();

            Optional<Attendance> attendance = attendanceRepository.findByStudentIdAndAttendedDateTimeBetween(
                    attendanceUpdateRequest.getStudentId(), startDateTime, endDateTime);
            if (attendance.isPresent()) {
                attendance.get().update(attendanceType, note);
                attendanceList.add(attendance.get());
            }
            if (attendance.isEmpty()) {
                Student student = studentSubService.findStudentByStudentId(attendanceUpdateRequest.getStudentId());

                Attendance newAttendance = new Attendance(
                        attendanceUpdateRequest.getAttendedDateTime(),
                        attendanceType,
                        note,
                        student,
                        attendedSection.get());
                attendanceList.add(newAttendance);
            }
        }
        attendanceRepository.saveAll(attendanceList);

        List<AttendanceResponse> attendanceResponseList = attendanceList.stream()
                .map(AttendanceResponse::new)
                .toList();

        return new AttendanceAllResponse(attendanceResponseList, attendanceList.size());
    }

    // 일일 출석 조회 메소드 (학생)
    @Transactional
    public AttendanceResponse getAttendanceByStudent(Long studentId, LocalDateTime selectedDay) {

        LocalDate targetDate = selectedDay.toLocalDate();
        LocalDateTime startDateTime = targetDate.atStartOfDay();
        LocalDateTime endDateTime = targetDate.atTime(23, 59, 59);

        Optional<Attendance> result = attendanceRepository.findByStudentIdAndAttendedDateTimeBetween(studentId, startDateTime, endDateTime);
        if (result.isEmpty()) {
            throw new MyException(ErrorCode.ATTENDANCE_NOT_FOUND);
        }

        return new AttendanceResponse(result.get());
    }

    // 일일 출석 조회 메소드 (반)
    @Transactional
    public AttendanceAllResponse getAllAttendancesBySection(Long sectionId, LocalDateTime selectedDay) {

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new MyException(ErrorCode.SECTION_NOT_FOUND));
        LocalDate targetDate = selectedDay.toLocalDate();
        LocalDateTime startDateTime = targetDate.atStartOfDay();
        LocalDateTime endDateTime = targetDate.atTime(23, 59, 59);

        List<Attendance> attendanceList = attendanceRepository.findBySectionIdAndAttendedDateTimeBetween(sectionId, startDateTime, endDateTime);
        List<Student> studentList = studentSubService.getStudentsBySectionId(sectionId);
        List<AttendanceResponse> attendanceResponseList = new ArrayList<>();
        for (Student student : studentList) {
            Optional<Attendance> attendance = attendanceRepository.findBySectionIdAndStudentIdAndAttendedDateTimeBetween(sectionId, student.getId(), startDateTime, endDateTime);
            AttendanceResponse attendanceResponse = new AttendanceResponse();
            if (attendance.isEmpty()) {
                attendanceResponse = new AttendanceResponse(
                        startDateTime,
                        student.getId(),
                        student.getName(),
                        section.getName(),
                        student.getStudentPhoneNumber(),
                        student.getParentPhoneNumber(),
                        null,
                        " ");
            }
            log.info("parentPhoneNumber: " + attendanceResponse.getParentPhoneNumber());
            if (attendance.isPresent()) {
                attendanceResponse = new AttendanceResponse(
                        attendance.get().getAttendedDateTime(),
                        student.getId(),
                        student.getName(),
                        section.getName(),
                        student.getStudentPhoneNumber(),
                        student.getParentPhoneNumber(),
                        attendance.get().getAttendanceType(),
                        attendance.get().getNote());
            }
            attendanceResponseList.add(attendanceResponse);
        }

        return new AttendanceAllResponse(attendanceResponseList, attendanceList.size());
    }
}
