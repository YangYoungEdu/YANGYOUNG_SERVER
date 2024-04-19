package com.yangyoung.server.lectureDate.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureDateRepository extends JpaRepository<LectureDate, Long> {

    List<LectureDate> findByLectureId(Long lectureId);
}
