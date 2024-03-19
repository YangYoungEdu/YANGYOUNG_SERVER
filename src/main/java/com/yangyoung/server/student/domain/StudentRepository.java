package com.yangyoung.server.student.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

//    Optional<Student> findByStudentId(Long studentId);

    List<Student> findBySectionId(Long sectionId);

    List<Student> findBySectionIdIn(List<Long> sectionIdList);
}
