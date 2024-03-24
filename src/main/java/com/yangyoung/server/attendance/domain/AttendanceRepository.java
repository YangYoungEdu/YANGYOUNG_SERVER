package com.yangyoung.server.attendance.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudentIdAndAttendedDateTimeBetween(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime);

<<<<<<< HEAD
    Optional<Attendance> findBySectionIdAndStudentIdAndAttendedDateTimeBetween(Long sectionId, Long studentId, LocalDateTime startDateTime, LocalDateTime endDateTime);
=======
    Optional<Attendance> findByStudentIdAndSectionIdAndAttendedDateTimeBetween(Long studentId, Long sectionId, LocalDateTime startDateTime, LocalDateTime endDateTime);
>>>>>>> release/1.2.0

    List<Attendance> findBySectionIdAndAttendedDateTimeBetween(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
