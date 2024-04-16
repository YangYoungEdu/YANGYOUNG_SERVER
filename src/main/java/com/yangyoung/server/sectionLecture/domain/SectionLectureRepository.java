package com.yangyoung.server.sectionLecture.domain;

import com.yangyoung.server.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionLectureRepository extends JpaRepository<SectionLecture, Long> {

    List<SectionLecture> findBySectionId(Long sectionId);

    List<SectionLecture> findByLectureId(Long lectureId);

    @Query("SELECT sl.lecture FROM SectionLecture sl WHERE sl.section.id IN :sectionIdList")
    List<Lecture> findLectureBySectionIdIn(List<Long> sectionIdList);
}
