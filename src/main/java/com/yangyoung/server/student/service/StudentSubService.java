package com.yangyoung.server.student.service;

import com.yangyoung.server.exception.student.StudentNotFoundException;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.studentSection.domain.StudentSection;
import com.yangyoung.server.studentSection.domain.StudentSectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentSubService {

    private final StudentRepository studentRepository;
    private final StudentSectionRepository studentSectionRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentSubService.class);

    // id에 해당하는 학생이 찾기
    @Transactional
    public Student findStudentByStudentId(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            String message = String.format("Student not found. (studentId: %d)", studentId);
            logger.info(message);
            throw new StudentNotFoundException(message);
        }
        return student.get();
    }

    // 반 id로 학생 엔티티 조회
    @Transactional
    public List<Student> getStudentsBySectionId(Long sectionId) {
        return studentSectionRepository.findAllBySectionId(sectionId).stream()
                .map(StudentSection::getStudent)
                .toList();
    }
}
