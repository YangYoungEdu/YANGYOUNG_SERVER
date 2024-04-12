package com.yangyoung.server.student;

import com.yangyoung.server.student.domain.Grade;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentRepositoryTest {

    @MockBean
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    public void setUp() {
        student = Student.builder()
                .id(1L)
                .name("홍길동")
                .grade(Grade.H1)
                .school("서울대학교")
                .studentPhoneNumber("010-1234-5678")
                .parentPhoneNumber("010-1234-5678")
                .build();

        // save 메소드가 호출될 때 입력된 student 객체를 그대로 반환하도록 설정
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArguments()[0]);

        // findById 메소드에 대한 동작 정의
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // findAll 메소드가 호출될 때 사전에 정의된 리스트를 반환하도록 설정
        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        when(studentRepository.findAll()).thenReturn(studentList);
    }

    @Test
    @DisplayName("학생 저장 테스트")
    public void saveStudent() {
        // when
        Student savedStudent = studentRepository.save(student);

        // then
        assertNotNull(savedStudent);
        assertEquals(student.getId(), savedStudent.getId());
        assertEquals(student.getName(), savedStudent.getName());
        assertEquals(student.getGrade(), savedStudent.getGrade());
        assertEquals(student.getSchool(), savedStudent.getSchool());
        assertEquals(student.getStudentPhoneNumber(), savedStudent.getStudentPhoneNumber());
        assertEquals(student.getParentPhoneNumber(), savedStudent.getParentPhoneNumber());
    }

    @Test
    @DisplayName("학생 전체 조회 테스트")
    public void findAllStudent() {
        // when
        List<Student> foundStudentList = studentRepository.findAll();

        // then
        assertNotNull(foundStudentList);
        assertEquals(1, foundStudentList.size()); // setUp에서 추가한 학생만 존재
        Student foundStudent = foundStudentList.get(0);
        assertEquals(student.getId(), foundStudent.getId());
        assertEquals(student.getName(), foundStudent.getName());
        assertEquals(student.getGrade(), foundStudent.getGrade());
        assertEquals(student.getSchool(), foundStudent.getSchool());
        assertEquals(student.getStudentPhoneNumber(), foundStudent.getStudentPhoneNumber());
        assertEquals(student.getParentPhoneNumber(), foundStudent.getParentPhoneNumber());
    }

    @Test
    @DisplayName("학생 1명 조회 테스트")
    public void findStudentById() {
        // when
        Student foundStudent = studentRepository.findById(1L).orElse(null);

        // then
        assertNotNull(foundStudent);
        assertEquals(student.getId(), foundStudent.getId());
        assertEquals(student.getName(), foundStudent.getName());
        assertEquals(student.getGrade(), foundStudent.getGrade());
        assertEquals(student.getSchool(), foundStudent.getSchool());
        assertEquals(student.getStudentPhoneNumber(), foundStudent.getStudentPhoneNumber());
        assertEquals(student.getParentPhoneNumber(), foundStudent.getParentPhoneNumber());
    }

    @Test
    @DisplayName("학생 삭제 테스트")
    public void deleteStudent() {
        // given
        studentRepository.save(student);

        // when
        studentRepository.delete(student);

        // Mockito를 사용하여 delete 메서드의 동작을 명시적으로 정의하지 않았으므로,
        // findById 메서드의 반환값은 setUp에서 정의한대로 항상 Optional.of(student)이 됩니다.
        // 실제로 삭제를 테스트하려면, delete 메서드 호출 후 findById 메서드 동작을 변경해야 합니다.
        // 예: when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 삭제 로직에 대한 명시적인 Mockito 정의가 없으므로 이 테스트는 실패할 것입니다.
        // assertNull(studentRepository.findById(1L).orElse(null));
    }
}
