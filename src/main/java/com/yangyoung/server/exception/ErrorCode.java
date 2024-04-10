package com.yangyoung.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    STUDENT_DAY_MISMATCH(HttpStatus.BAD_REQUEST, "학생이 수강하는 수업이 있는 날이 아닙니다"),
    STUDENT_TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 학생의 과제가 없습니다"),
    SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 반이 없습니다"),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 학생이 없습니다"),
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 강의가 없습니다"),
    LECTURE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 강의입니다"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러가 발생하였습니다"),
    FORM_NOT_FILLED(HttpStatus.BAD_REQUEST, "양식을 모두 채워주세요"),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 과제가 없습니다"),
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 출석이 없습니다"),
    DAY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 요일이 없습니다"),
    ATTENDANCE_ALREADY_CHECKED(HttpStatus.BAD_REQUEST, "이미 출석이 되어있습니다");

    private HttpStatus status;
    private String message;
}
