package com.yangyoung.server.student.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.dto.response.SectionBriefResponse;
import com.yangyoung.server.student.domain.Grade;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.student.dto.response.SearchOptionResponse;
import com.yangyoung.server.student.dto.response.StudentAllResponse;
import com.yangyoung.server.student.dto.response.StudentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentSubService {

    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;

    // id에 해당하는 학생이 존재하는지 확인
    public Student isStudentExist(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new MyException(ErrorCode.STUDENT_NOT_FOUND));
    }

    // 학생 인적 사항 추가
    public Student createStudentInfo(Long id, String name, String school, Grade grade, String studentPhoneNumber, String parentPhoneNumber) {

        Optional<Student> isExisted = studentRepository.findById(id); // 학생 id 중복 확인
        if (isExisted.isPresent()) {
            throw new MyException(ErrorCode.STUDENT_ID_ALREADY_EXIST);
        }

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
    public void assignStudentToSection(Student student, Long sectionId) {

        Section section = sectionRepository.findById(sectionId) // 반 존재 여부 확인
                .orElseThrow(() -> new MyException(ErrorCode.SECTION_NOT_FOUND));

        student.assignedToSection(section);
        studentRepository.save(student);
    }

    // 학생 전체 인적 사항 조회
    public List<StudentResponse> readAllStudentInfo(List<Student> studentList) {

        return studentList.stream()
                .map(student -> readStudentInfo(student.getId()))
                .filter(Objects::nonNull)
                .toList();
    }

    // 학생 인적 사항 조회
    public StudentResponse readStudentInfo(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new MyException(ErrorCode.STUDENT_NOT_FOUND));

        return new StudentResponse(student.getId(),
                student.getName(),
                student.getSchool(),
                student.getGrade().getGradeName(),
                student.getStudentPhoneNumber(),
                student.getParentPhoneNumber(),
                student.getSection().getName(),
                student.getSection().getId());
    }
}
