package com.yangyoung.server.exception.section;

import com.yangyoung.server.exception.CommonException;

public class SectionNotFoundException extends CommonException {
    public SectionNotFoundException(String message) {
        super(message);
    }
}
