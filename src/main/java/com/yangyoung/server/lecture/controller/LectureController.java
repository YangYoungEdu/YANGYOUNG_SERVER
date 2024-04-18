package com.yangyoung.server.lecture.controller;

import com.yangyoung.server.lecture.dto.request.LectureCreateRequest;
import com.yangyoung.server.lecture.dto.request.LectureSeqUpdateRequest;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureResponse;
import com.yangyoung.server.lecture.dto.response.LecturePostResponse;
import com.yangyoung.server.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/lecture")
public class LectureController {

    private final LectureService lectureService;


    // 강의 등록 컨트롤러
    @PostMapping("")
    public ResponseEntity<LecturePostResponse> postLecture(@RequestBody final LectureCreateRequest request) {

        LecturePostResponse response = lectureService.createLecture(request);

        return ResponseEntity.ok()
                .body(response);

    }

    // 전체 강의 조회 컨트롤러
    @GetMapping("")
    public ResponseEntity<LectureAllResponse> getAllLectures() {

        LectureAllResponse response = lectureService.getAllLectures();

        return ResponseEntity.ok()
                .body(response);

    }

    // 강의 순서 변경 컨트롤러
    @PatchMapping("/seq")
    public ResponseEntity<Void> updateLectureSeq(@RequestBody final LectureSeqUpdateRequest request) {

        lectureService.updateLectureSeq(request);

        return ResponseEntity.noContent().build();
    }

    // 반 별 강의 조회 컨트롤러
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureResponse> getOneLecture(@PathVariable(value = "lectureId") final Long lectureId) {

        LectureResponse response = lectureService.getOneLecture(lectureId);

        return ResponseEntity.ok()
                .body(response);

    }

    // 강의 삭제 컨트롤러
    @DeleteMapping("/{lectureId}")
    public ResponseEntity<Void> deleteLecture(@PathVariable(value = "lectureId") final Long lectureId) {

        lectureService.deleteLecture(lectureId);

        return ResponseEntity.noContent().build();
    }

}
