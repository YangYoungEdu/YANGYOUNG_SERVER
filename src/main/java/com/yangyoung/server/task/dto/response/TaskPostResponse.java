package com.yangyoung.server.task.dto.response;

import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.task.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskPostResponse {

    private String assignment;

    private String sectionName;

    private String studentName;

    public TaskPostResponse(Task task, Section section) {
        this.assignment = task.getContent();
        this.sectionName = section.getName();
    }

    public TaskPostResponse(Task task, Student student) {
        this.assignment = task.getContent();
        this.studentName = student.getName();
    }
}
