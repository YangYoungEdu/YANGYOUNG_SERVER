package com.yangyoung.server.section.controller;

import com.yangyoung.server.section.dto.request.SectionCreateRequest;
import com.yangyoung.server.section.dto.request.SectionStudentUpdateRequest;
import com.yangyoung.server.section.dto.request.SectionUpdateRequest;
import com.yangyoung.server.section.dto.response.*;
import com.yangyoung.server.section.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/section")
public class SectionController {

    private final SectionService sectionService;

    // 반 생성 컨트롤러
    @PostMapping("")
    public ResponseEntity<SectionResponse> postSection(@RequestBody final SectionCreateRequest request) {

        final SectionResponse response = sectionService.createSection(request);

        return ResponseEntity.ok()
                .body(response);
    }

    // 전체 반 정보 조회
    @GetMapping("")
    public ResponseEntity<SectionAllResponse> getAllSections() {

        final SectionAllResponse response = sectionService.readAllSections();

        return ResponseEntity.ok()
                .body(response);

    }

    // 반 상세 조회
    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionDetailResponse> getOneSection(@PathVariable final Long sectionId) {

        final SectionDetailResponse response = sectionService.readSectionLecture(sectionId);

        return ResponseEntity.ok()
                .body(response);

    }

    // 반 삭제
    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteSection(@PathVariable final Long sectionId) {

        sectionService.deleteSection(sectionId);

        return ResponseEntity.noContent().build();
    }

    // 반 정보 수정
    @PatchMapping("")
    public ResponseEntity<SectionResponse> updateSection(@RequestBody final SectionUpdateRequest request) {

        final SectionResponse response = sectionService.updateSection(request);

        return ResponseEntity.ok()
                .body(response);
    }

    // 반 학생 추가 컨트롤러
    @PatchMapping("/student")
    public ResponseEntity<Void> updateSectionStudent(@RequestBody final SectionStudentUpdateRequest request) {

        sectionService.updateSectionStudent(request);

        return ResponseEntity.ok()
                .build();
    }

    // 반 학생 삭제 컨트롤러
    @DeleteMapping("/student")
    public ResponseEntity<Void> deleteSectionStudent(@RequestParam(value = "sectionId") Long sectionId, @RequestParam(value = "studentIdList") List<Long> studentIdList) {

        sectionService.deleteSectionStudents(sectionId, studentIdList);

        return ResponseEntity.ok()
                .build();
    }
}
