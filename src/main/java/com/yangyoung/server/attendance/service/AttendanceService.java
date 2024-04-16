package com.yangyoung.server.attendance.service;

import com.yangyoung.server.attendance.domain.Attendance;
import com.yangyoung.server.attendance.domain.AttendanceRepository;
import com.yangyoung.server.attendance.domain.AttendanceType;
import com.yangyoung.server.attendance.dto.request.AttendanceSectionRequest;
import com.yangyoung.server.attendance.dto.request.AttendanceStudentRequest;
import com.yangyoung.server.attendance.dto.request.AttendanceUpdateRequest;
import com.yangyoung.server.attendance.dto.response.AttendanceAllResponse;
import com.yangyoung.server.attendance.dto.response.AttendanceResponse;
import com.yangyoung.server.exception.attendance.AttendanceNotFoundException;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionUtilService;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.service.StudentUtilService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final StudentUtilService studentUtilService;
    private final SectionUtilService sectionUtilService;

    // 출석 체크 메소드 (학생)
    @Transactional
    public void attend(AttendanceStudentRequest request) {

        LocalDateTime now = LocalDateTime.now().plusHours(9);
        LocalDateTime start = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(23).withMinute(59).withSecond(59);

        Student attendedStudent = studentUtilService.findStudentByStudentId(request.getStudentId());
        List<Section> sectionList = sectionUtilService.findSectionsByStudentId(request.getStudentId());
        List<Attendance> attendanceList = new ArrayList<>();
        for (Section section : sectionList) {
            Optional<Attendance> check = attendanceRepository.findByStudentIdAndSectionIdAndAttendedDateTimeBetween(
                    request.getStudentId(), section.getId(), start, end);
            if (check.isPresent()) {
                check.get().updateAttendanceType(AttendanceType.ATTENDANCE);
                attendanceRepository.save(check.get());
            }
            Attendance attendance = new Attendance(
                    now,
                    AttendanceType.ATTENDANCE,
                    request.getNote(),
                    attendedStudent,
                    section);
            attendanceList.add(attendance);
        }
        attendanceRepository.saveAll(attendanceList);
    }

    // 출석 체크 메소드 (반)
    @Transactional
    public AttendanceAllResponse attendBySection(AttendanceSectionRequest request) {

        LocalDate targetDate;
        if (request.getAttendanceUpdateRequestList().isEmpty()) {
            targetDate = LocalDate.now();
        } else {
            targetDate = request.getAttendanceUpdateRequestList().get(0).getAttendedDateTime().plusHours(9).toLocalDate();
        }
        LocalDateTime startDateTime = targetDate.atStartOfDay();
        LocalDateTime endDateTime = targetDate.atTime(23, 59, 59);

        Section attendedSection = sectionUtilService.findSectionBySectionId(request.getSectionId());

        List<Attendance> attendanceList = new ArrayList<>();
        List<AttendanceUpdateRequest> attendanceStudentRequestedList = request.getAttendanceUpdateRequestList();
        for (AttendanceUpdateRequest attendanceUpdateRequest : attendanceStudentRequestedList) {

            AttendanceType attendanceType = attendanceUpdateRequest.getAttendanceType();
            String note = attendanceUpdateRequest.getNote();

            Optional<Attendance> attendance = attendanceRepository.findByStudentIdAndSectionIdAndAttendedDateTimeBetween(
                    attendanceUpdateRequest.getStudentId(),
                    attendanceUpdateRequest.getStudentId(), startDateTime, endDateTime);
            if (attendance.isPresent()) {
                log.info("isPresent");
                attendance.get().update(attendanceType, note);
                attendanceList.add(attendance.get());
            }
            if (attendance.isEmpty()) {
                log.info("isEmpty");
                Student student = studentUtilService.findStudentByStudentId(attendanceUpdateRequest.getStudentId());

                Attendance newAttendance = new Attendance(
                        attendanceUpdateRequest.getAttendedDateTime(),
                        attendanceType,
                        note,
                        student,
                        attendedSection);
                attendanceRepository.save(newAttendance);
                log.info("newAttendance: " + newAttendance);
                attendanceList.add(newAttendance);
            }
        }
//        attendanceRepository.saveAll(attendanceList);

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
            String message = String.format("Attendance not found. (studentId: %d)", studentId);
            log.info(message);
            throw new AttendanceNotFoundException(message);
        }

        return new AttendanceResponse(result.get());
    }

    // 일일 출석 조회 메소드 (반)
    @Transactional
    public AttendanceAllResponse getAllAttendancesBySection(Long sectionId, LocalDateTime selectedDay) {

        Section section = sectionUtilService.findSectionBySectionId(sectionId);

        LocalDate targetDate = selectedDay.toLocalDate();
        LocalDateTime startDateTime = targetDate.atStartOfDay();
        LocalDateTime endDateTime = targetDate.atTime(23, 59, 59);

        // 각 학생별로 최신의 출석 정보를 유지하는 Map 생성
        Map<Long, Attendance> latestAttendanceMap = new HashMap<>();
        attendanceRepository.findBySectionIdAndAttendedDateTimeBetween(sectionId, startDateTime, endDateTime)
                .forEach(attendance -> {
                    Long studentId = attendance.getStudent().getId();
                    latestAttendanceMap.put(studentId, attendance); // 최신 정보 업데이트
                });

        List<Student> studentList = studentUtilService.getStudentsBySectionId(sectionId);
        List<AttendanceResponse> attendanceResponseList = new ArrayList<>();

        for (Student student : studentList) {
            Attendance latestAttendance = latestAttendanceMap.get(student.getId());
            AttendanceResponse attendanceResponse = new AttendanceResponse(
                    latestAttendance != null ? latestAttendance.getAttendedDateTime() : startDateTime,
                    student.getId(),
                    student.getName(),
                    section.getName(),
                    student.getStudentPhoneNumber(),
                    student.getParentPhoneNumber(),
                    latestAttendance != null ? latestAttendance.getAttendanceType() : null,
                    latestAttendance != null ? latestAttendance.getNote() : " "
            );
            attendanceResponseList.add(attendanceResponse);
        }

        return new AttendanceAllResponse(attendanceResponseList, latestAttendanceMap.size());
    }


//    @Transactional
//    public AttendanceAllResponse getAllAttendancesBySection(Long sectionId, LocalDateTime selectedDay) {
//
//        Section section = sectionRepository.findById(sectionId)
//                .orElseThrow(() -> new MyException(ErrorCode.SECTION_NOT_FOUND));
//        LocalDate targetDate = selectedDay.toLocalDate();
//        LocalDateTime startDateTime = targetDate.atStartOfDay();
//        LocalDateTime endDateTime = targetDate.atTime(23, 59, 59);
//
//        List<Attendance> attendanceList = attendanceRepository.findBySectionIdAndAttendedDateTimeBetween(sectionId, startDateTime, endDateTime);
//        for (Attendance attendance : attendanceList) {
//            log.info("attendance: " + attendance.getStudent().getName());
//        }
//        List<Student> studentList = studentSubService.getStudentsBySectionId(sectionId);
//        List<AttendanceResponse> attendanceResponseList = new ArrayList<>();
//        for (Student student : studentList) {
//            Optional<Attendance> attendance = attendanceRepository.findByStudentIdAndSectionIdAndAttendedDateTimeBetween(student.getId(), sectionId, startDateTime, endDateTime);
//            AttendanceResponse attendanceResponse = new AttendanceResponse();
//            if (attendance.isEmpty()) {
//                attendanceResponse = new AttendanceResponse(
//                        startDateTime,
//                        student.getId(),
//                        student.getName(),
//                        section.getName(),
//                        student.getStudentPhoneNumber(),
//                        student.getParentPhoneNumber(),
//                        null,
//                        " ");
//            }
//            log.info("parentPhoneNumber: " + attendanceResponse.getParentPhoneNumber());
//            if (attendance.isPresent()) {
//                attendanceResponse = new AttendanceResponse(
//                        attendance.get().getAttendedDateTime(),
//                        student.getId(),
//                        student.getName(),
//                        section.getName(),
//                        student.getStudentPhoneNumber(),
//                        student.getParentPhoneNumber(),
//                        attendance.get().getAttendanceType(),
//                        attendance.get().getNote());
//            }
//            attendanceResponseList.add(attendanceResponse);
//        }
//
//        return new AttendanceAllResponse(attendanceResponseList, attendanceList.size());
//    }
}
