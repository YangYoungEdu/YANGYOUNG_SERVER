package com.yangyoung.server.student.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.student.domain.Grade;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.student.dto.response.StudentResponse;
import com.yangyoung.server.studentSection.domain.StudentSection;
import com.yangyoung.server.studentSection.domain.StudentSectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentSubService {

    private final StudentRepository studentRepository;
    private final StudentSectionRepository studentSectionRepository;

    private final SectionSubService sectionSubService;

    // id에 해당하는 학생이 찾기
    @Transactional
    public Student findStudentByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new MyException(ErrorCode.STUDENT_NOT_FOUND));
    }

    // id 중복 체크
    @Transactional
    public void isStudentIdExist(Long studentId) {
        Optional<Student> isExisted = studentRepository.findById(studentId);
        if (isExisted.isPresent()) {
            throw new MyException(ErrorCode.STUDENT_ID_ALREADY_EXIST);
        }
    }

    // 학생 인적 사항 추가
    @Transactional
    public Student createStudentInfo(Long id, String name, String school, Grade grade, String studentPhoneNumber, String parentPhoneNumber) {

        isStudentIdExist(id);

        return studentRepository.save(Student.builder()
                .id(id)
                .name(name)
                .school(school)
                .grade(grade)
                .studentPhoneNumber(studentPhoneNumber)
                .parentPhoneNumber(parentPhoneNumber)
                .build());
    }

    // 학생 반 할당
    @Transactional
    public void assignStudentToSection(Student student, List<Long> sectionIdList) {

        List<Section> sectionList = sectionSubService.findSectionsBySectionIdList(sectionIdList);
        List<StudentSection> studentSectionList = sectionList.stream()
                .map(section -> StudentSection.builder()
                        .student(student)
                        .section(section)
                        .build())
                .toList();
        studentSectionRepository.saveAll(studentSectionList);
    }

    // 학생 전체 인적 사항 조회
    @Transactional
    public List<StudentResponse> readAllStudentInfo(List<Student> studentList) {

        List<StudentResponse> studentResponseList = new ArrayList<>();
        for (Student student : studentList) {
            List<String> sectionNameList = sectionSubService.findSectionNamesByStudentId(student.getId());
            StudentResponse studentResponse = new StudentResponse(student, sectionNameList);
            studentResponseList.add(studentResponse);
        }

        return studentResponseList;
    }

    // 학생 인적 사항 조회
    @Transactional
    public StudentResponse getStudentInfo(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new MyException(ErrorCode.STUDENT_NOT_FOUND));

        List<String> sectionNameList = sectionSubService.findSectionNamesByStudentId(studentId);

        return new StudentResponse(student, sectionNameList);
    }

    // 반 id로 학생 엔티티 조회
    @Transactional
    public List<Student> getStudentsBySectionId(Long sectionId) {
        return studentSectionRepository.findAllBySectionId(sectionId).stream()
                .map(StudentSection::getStudent)
                .toList();
    }
}
