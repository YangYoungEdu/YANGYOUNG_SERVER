package com.yangyoung.server.exception.lecture;

import com.yangyoung.server.exception.CommonException;

public class LectureNotFoundException extends CommonException {
    public LectureNotFoundException(String message) {
        super(message);
    }
}
