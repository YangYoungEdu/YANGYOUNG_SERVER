package com.yangyoung.server.studentSection.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentSectionRepository extends JpaRepository<StudentSection, Long> {

    List<StudentSection> findAllByStudentId(Long studentId);

    List<StudentSection> findAllBySectionId(Long sectionId);

    Optional<StudentSection> findBySectionIdAndStudentId(Long sectionId, Long studentId);
}
