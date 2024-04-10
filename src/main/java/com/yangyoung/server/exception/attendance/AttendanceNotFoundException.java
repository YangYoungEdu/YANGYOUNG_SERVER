package com.yangyoung.server.exception.attendance;

import com.yangyoung.server.exception.CommonException;

public class AttendanceNotFoundException extends CommonException {
    public AttendanceNotFoundException(String message) {
        super(message);
    }
}
