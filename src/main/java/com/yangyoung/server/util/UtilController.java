package com.yangyoung.server.util;

import com.yangyoung.server.student.dto.response.SearchOptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/util")
public class UtilController {

    private final UtilService utilService;

    // 검색 옵션 리스트 조회 API
    @RequestMapping("/searchOption")
    public ResponseEntity<SearchOptionResponse> getSearchOptionList() {
        return ResponseEntity.ok(utilService.getSearchOptionList());
    }

    // 파일 업로드 API
    @PostMapping("/uploadFiles")
    public ResponseEntity<List<String>> uploadFiles(List<MultipartFile> multipartFiles, String directoryPath) throws IOException {
        return ResponseEntity.ok(utilService.uploadFiles(multipartFiles, directoryPath));
    }
}
