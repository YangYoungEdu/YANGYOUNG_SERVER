package com.yangyoung.server.task.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yangyoung.server.configuration.BaseEntity;
import com.yangyoung.server.sectionTask.domain.SectionTask;
import com.yangyoung.server.studentTask.domain.StudentTask;
import com.yangyoung.server.studentTask.domain.TaskProgress;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assignment;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<StudentTask> studentTaskList;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<SectionTask> sectionTaskList;

    @Builder
    public Task(String assignment) {
        this.assignment = assignment;
    }
}
