package com.yangyoung.server.task.domain;

import lombok.Getter;

@Getter
public enum TaskType {

    STUDENT(1, "개인"),
    SECTION(2, "반");

    private final int typeNumber;
    private final String typeName;

    TaskType(int typeNumber, String typeName) {
        this.typeNumber = typeNumber;
        this.typeName = typeName;
    }
}
