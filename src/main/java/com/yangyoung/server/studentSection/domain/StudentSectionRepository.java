package com.yangyoung.server.studentSection.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentSectionRepository extends JpaRepository<StudentSection, Long> {

    List<StudentSection> findAllByStudentId(Long studentId);

    List<StudentSection> findAllBySectionId(Long sectionId);
}
