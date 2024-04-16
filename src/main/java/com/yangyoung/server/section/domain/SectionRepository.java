package com.yangyoung.server.section.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByIdIn(List<Long> sectionIdList); // id 리스트로 섹션 조회

}
