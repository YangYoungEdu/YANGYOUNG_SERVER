package com.yangyoung.server.exception.student;

import com.yangyoung.server.exception.CommonException;

public class StudentNotFoundException extends CommonException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
