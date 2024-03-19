package com.yangyoung.server.sectionLecture.controller;

import com.yangyoung.server.sectionLecture.dto.request.EnrollLectureCreateRequest;
import com.yangyoung.server.sectionLecture.dto.response.EnrollLecturePostResponse;
import com.yangyoung.server.sectionLecture.service.SectionLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v0/sectionLecture")
public class SectionLectureController {

    private final SectionLectureService sectionLectureService;

    @PostMapping("")
    public ResponseEntity<EnrollLecturePostResponse> postSectionLecture(@RequestBody final EnrollLectureCreateRequest request) {

        EnrollLecturePostResponse response = sectionLectureService.enrollLecture(request);

        return ResponseEntity.ok()
                .body(response);

    }
}
