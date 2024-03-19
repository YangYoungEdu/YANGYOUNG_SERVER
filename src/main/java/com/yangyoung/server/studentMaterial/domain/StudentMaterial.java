package com.yangyoung.server.studentMaterial.domain;

import com.yangyoung.server.material.domain.Material;
import com.yangyoung.server.student.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "student_material")
public class StudentMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isPrinted;

    @ManyToOne()
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne()
    @JoinColumn(name = "student_id")
    private Student student;
}
