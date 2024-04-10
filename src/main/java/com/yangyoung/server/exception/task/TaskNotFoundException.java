package com.yangyoung.server.exception.task;

import com.yangyoung.server.exception.CommonException;

public class TaskNotFoundException extends CommonException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
