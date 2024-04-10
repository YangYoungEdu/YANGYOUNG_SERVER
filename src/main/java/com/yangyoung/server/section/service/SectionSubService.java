package com.yangyoung.server.section.service;

import com.yangyoung.server.exception.section.SectionNotFoundException;
import com.yangyoung.server.exception.student.StudentNotFoundException;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.dto.response.SectionResponse;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.studentSection.domain.StudentSection;
import com.yangyoung.server.studentSection.domain.StudentSectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionSubService {

    private final SectionRepository sectionRepository;
    private final StudentSectionRepository studentSectionRepository;
    private final StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(SectionSubService.class);

    // id에 해당하는 반 정보 조회 - 단일
    @Transactional
    public Section findSectionBySectionId(Long sectionId) {
        Optional<Section> section = sectionRepository.findById(sectionId);
        if (section.isEmpty()) { /* 해당하는 반이 없는 경우 */
            String message = String.format("Section not found. (studentId: %d)", sectionId);
            logger.info(message);
            throw new SectionNotFoundException(message);
        }
        return section.get();
    }

    // id에 해당하는 반 정보 조회 - 다중
    @Transactional
    public List<Section> findSectionsBySectionIdList(List<Long> sectionIdList) {
        return sectionRepository.findAllById(sectionIdList);
    }

    // id에 해당하는 반 정보 조회
    @Transactional
    public SectionResponse readSectionInfo(Long sectionId) {

        Optional<Section> section = sectionRepository.findById(sectionId);
        if (section.isEmpty()) { /* 해당하는 반이 없는 경우 */

        }

        return new SectionResponse(section.get());
    }

    // 학생이 속한 반의 엔티티 조회
    @Transactional
    public List<Section> findSectionsByStudentId(Long studentId) {
        List<StudentSection> studentSectionList = studentSectionRepository.findByStudentId(studentId);
        return studentSectionList.stream()
                .map(StudentSection::getSection)
                .toList();
    }

    // 반에 학생 할당
    @Transactional
    public void assignStudentToSection(Long sectionId, List<Long> studentIdList) {

        Section section = findSectionBySectionId(sectionId);

        List<StudentSection> studentSectionList = new ArrayList<>();
        for (int i = 0; i < studentIdList.size(); i++) {
            Optional<StudentSection> studentSection = studentSectionRepository.findBySectionIdAndStudentId(sectionId, studentIdList.get(i));
            if (studentSection.isPresent()) {
                studentSectionList.add(studentSection.get());
            }
            if (studentSection.isEmpty()) {
                Optional<Student> student = studentRepository.findById(studentIdList.get(i));
                if(student.isEmpty()) {
                    String message = String.format("Student not found. (studentId: %d)", studentIdList.get(i));
                    logger.info(message);
                    throw new StudentNotFoundException(message);
                }
                StudentSection newStudentSection = StudentSection.builder()
                        .section(section)
                        .student(student.get())
                        .build();
                studentSectionList.add(newStudentSection);
            }
        }
        studentSectionRepository.saveAll(studentSectionList);
    }
}

