package com.yangyoung.server.sectionTask.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SectionTaskRepository extends JpaRepository<SectionTask, Long> {

    List<SectionTask> findBySectionId(Long sectionId);

    List<SectionTask> findBySectionIdAndTask_TaskDate(Long sectionId, LocalDate taskDate);
}
