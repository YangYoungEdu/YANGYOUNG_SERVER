package com.yangyoung.server.material.domain;

import com.yangyoung.server.lectureMaterial.domain.LectureMaterial;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.studentMaterial.domain.StudentMaterial;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private LocalDate classDay;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentMaterial> studentMaterialList;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LectureMaterial> lectureMaterialList;
}
