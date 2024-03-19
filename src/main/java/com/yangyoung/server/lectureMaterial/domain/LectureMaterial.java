package com.yangyoung.server.lectureMaterial.domain;

import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.material.domain.Material;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "lecture_material")
public class LectureMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne()
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}
