package com.yangyoung.server.studentTask.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yangyoung.server.configuration.BaseEntity;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.task.domain.Task;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StudentTask extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonManagedReference
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonManagedReference
    private Task task;

    @Enumerated(EnumType.STRING)
    private TaskProgress taskProgress;

    @Builder
    public StudentTask(Student student, Task task, TaskProgress taskProgress) {
        this.student = student;
        this.task = task;
        this.taskProgress = taskProgress;
    }

    public void updateTaskProgress(TaskProgress taskProgress) {
        this.taskProgress = taskProgress;
    }

}
