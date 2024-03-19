package com.yangyoung.server.lecture.domain;

import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.section.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByName(String name);
}
