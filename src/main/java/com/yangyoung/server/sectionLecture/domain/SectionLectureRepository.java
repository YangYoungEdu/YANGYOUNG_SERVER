package com.yangyoung.server.sectionLecture.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionLectureRepository extends JpaRepository<SectionLecture, Long> {

    List<SectionLecture> findBySectionId(Long sectionId);

    List<SectionLecture> findByLectureId(Long lectureId);

}
