package com.yangyoung.server.sectionLecture.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yangyoung.server.configuration.BaseEntity;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.lecture.domain.Lecture;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SectionLecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "section_id")
    @JsonManagedReference
    private Section section;

    @ManyToOne()
    @JoinColumn(name = "lecture_id")
    @JsonManagedReference
    private Lecture lecture;

    @Builder
    public SectionLecture(Section section, Lecture lecture) {
        this.section = section;
        this.lecture = lecture;
    }
}
