package com.yangyoung.server.studentTask.domain;


import lombok.Getter;

@Getter
public enum TaskProgress {
    NOT_STARTED(1, "NOT_STARTED"),
    IN_PROGRESS(2, "IN_PROGRESS"),
    COMPLETED(3, "COMPLETED");

    private final int progressNumber;
    private final String progressName;

    TaskProgress(int progressNumber, String progressName) {
        this.progressNumber = progressNumber;
        this.progressName = progressName;
    }
}
