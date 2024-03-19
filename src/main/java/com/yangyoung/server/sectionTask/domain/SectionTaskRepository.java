package com.yangyoung.server.sectionTask.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionTaskRepository extends JpaRepository<SectionTask, Long> {

    List<SectionTask> findBySectionId(Long sectionId);
}
