package com.yangyoung.server.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.dto.response.SectionBriefResponse;
import com.yangyoung.server.student.domain.Student;
import com.yangyoung.server.student.domain.StudentRepository;
import com.yangyoung.server.student.dto.response.SearchOptionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilService {

    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 업로드 메소드
    @Transactional
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String directoryPath) throws IOException {

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename();
            log.info("originalFilename: " + originalFilename);

            // String fullFilePath = directoryPath + "/" + originalFilename;
            String fullFilePath = "material" + "/" + originalFilename;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(bucket, fullFilePath, multipartFile.getInputStream(), metadata);
            String fileUrl = amazonS3.getUrl(bucket, fullFilePath).toString();
            log.info(fileUrl);

            fileUrls.add(fileUrl);
        }

        return fileUrls;
    }

    // S3 파일 조회 메소드


    // 검색 옵션 리스트 조회 메소드
    @Transactional
    public SearchOptionResponse getSearchOptionList() {

        List<Student> studentList = studentRepository.findAll();
        List<Section> sectionList = sectionRepository.findAll();

        List<SectionBriefResponse> sectionNameList = sectionList.stream()
                .map(SectionBriefResponse::new)
                .toList();

        List<String> gradeList = studentList.stream()
                .map(student -> student.getGrade().getGradeName())
                .distinct()
                .toList();

        List<String> schoolList = studentList.stream()
                .map(Student::getSchool)
                .distinct()
                .toList();

        return new SearchOptionResponse(sectionNameList,
                gradeList, schoolList);
    }

    // 요일 한국어 변환 메소드
    @Transactional
    public String convertDayToKorean(String day) {
        return switch (day) {
            case "MONDAY" -> "월요일";
            case "TUESDAY" -> "화요일";
            case "WEDNESDAY" -> "수요일";
            case "THURSDAY" -> "목요일";
            case "FRIDAY" -> "금요일";
            case "SATURDAY" -> "토요일";
            case "SUNDAY" -> "일요일";
            default -> "요일 없음";
        };
    }
}
