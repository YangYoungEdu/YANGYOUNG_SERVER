package com.yangyoung.server.exception.student;

import com.yangyoung.server.exception.CommonException;

public class StudentIdDuplicatedException extends CommonException {
    public StudentIdDuplicatedException(String message) {
        super(message);
    }
}
