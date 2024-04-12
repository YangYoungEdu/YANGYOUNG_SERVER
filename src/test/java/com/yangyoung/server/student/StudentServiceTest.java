package com.yangyoung.server.student;

import com.yangyoung.server.student.service.StudentService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepositoryTest studentRepositoryTest;

    @InjectMocks
    private StudentService studentService;

}
