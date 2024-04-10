package com.yangyoung.server.studentSection.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentSectionRepository extends JpaRepository<StudentSection, Long> {

    List<StudentSection> findByStudentId(Long studentId);

    List<StudentSection> findAllBySectionId(Long sectionId);

    List<StudentSection> findAllByStudentIdAndSectionIdIn(Long studentId, List<Long> sectionIdList);

    Optional<StudentSection> findBySectionIdAndStudentId(Long sectionId, Long studentId);
}
