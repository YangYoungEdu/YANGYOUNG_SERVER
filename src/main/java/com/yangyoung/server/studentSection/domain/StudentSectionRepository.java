package com.yangyoung.server.studentSection.domain;

import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentSectionRepository extends JpaRepository<StudentSection, Long> {

    List<StudentSection> findByStudentId(Long studentId); // 학생별 수강 반 조회

    List<StudentSection> findBySectionId(Long sectionId); // 반별 수강 학생 조회

    Optional<StudentSection> findBySectionIdAndStudentId(Long sectionId, Long studentId); // 반별 수강 학생 조회

    @Query("SELECT ss.student FROM StudentSection ss WHERE ss.section.id = :sectionId")
    List<Student> findStudentsBySectionId(@Param("sectionId") Long sectionId);

    @Query("SELECT ss.section FROM StudentSection ss WHERE ss.student.id = :studentId")
    List<Section> findSectionByStudentId(@Param("studentId") Long studentId);
}
