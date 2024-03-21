package com.yangyoung.server.sectionTask.domain;

import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.studentTask.domain.TaskProgress;
import com.yangyoung.server.task.domain.Task;
import com.yangyoung.server.task.domain.TaskType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "section_task")
public class SectionTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Builder
    public SectionTask(Section section, Task task) {
        this.section = section;
        this.task = task;
    }
}
