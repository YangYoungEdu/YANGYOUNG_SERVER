package com.yangyoung.server.studentTask.domain;


import lombok.Getter;

@Getter
public enum TaskProgress {
    NOT_STARTED(1, "미시작"),
    IN_PROGRESS(2, "진행중"),
    COMPLETED(3, "완료"),
    ;

    private final int progressNumber;
    private final String progressName;

    TaskProgress(int progressNumber, String progressName) {
        this.progressNumber = progressNumber;
        this.progressName = progressName;
    }
}
