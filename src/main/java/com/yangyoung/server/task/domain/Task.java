package com.yangyoung.server.task.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yangyoung.server.configuration.BaseEntity;
import com.yangyoung.server.sectionTask.domain.SectionTask;
import com.yangyoung.server.studentTask.domain.StudentTask;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDate taskDate;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<StudentTask> studentTaskList;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<SectionTask> sectionTaskList;

    @Builder
    public Task(String content, LocalDate taskDate, TaskType taskType) {
        this.content = content;
        this.taskDate = taskDate;
        this.taskType = taskType;
    }

    public void updateTask(String content, LocalDate taskDate) {
        this.content = content;
        this.taskDate = taskDate;
    }
}
