package com.yangyoung.server.exception.lecture;

import com.yangyoung.server.exception.CommonException;

public class LectureNameDuplicateException extends CommonException {
    public LectureNameDuplicateException(String message) {
        super(message);
    }
}
