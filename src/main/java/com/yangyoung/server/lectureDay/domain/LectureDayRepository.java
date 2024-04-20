package com.yangyoung.server.lectureDay.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureDayRepository extends JpaRepository<LectureDay, Long> {

    List<LectureDay> findByLectureId(Long lectureId);
}
