package com.yangyoung.server.section.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.service.LectureService;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.dto.request.SectionCreateRequest;
import com.yangyoung.server.section.dto.request.SectionStudentChangeRequest;
import com.yangyoung.server.section.dto.request.SectionUpdateRequest;
import com.yangyoung.server.section.dto.response.*;
import com.yangyoung.server.sectionTask.dto.response.SectionTaskAllResponse;
import com.yangyoung.server.student.dto.response.StudentAllResponse;
import com.yangyoung.server.student.service.StudentService;
import com.yangyoung.server.task.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LectureService lectureService;
    private final StudentService studentService;
    private final SectionSubService sectionSubService;
    private final TaskService taskService;

    // 반 생성
    @Transactional
    public SectionResponse createSection(SectionCreateRequest request) {

        Section section = sectionRepository.save(request.toEntity());

        return new SectionResponse(section);
    }

    // 전체 반 조회
    @Transactional
    public SectionAllResponse readAllSections() {

        List<Section> sectionList = sectionRepository.findAll();
        List<SectionResponse> sectionResponseList = sectionList.stream()
                .map(SectionResponse::new)
                .toList();

        return new SectionAllResponse(sectionResponseList, sectionResponseList.size());
    }

    // 반 상세 정보 조회
    @Transactional
    public SectionDetailResponse readSectionLecture(Long sectionId) {

        SectionResponse sectionResponse = sectionSubService.readSectionInfo(sectionId);
        LectureAllResponse lectureAllResponse = lectureService.getLecturesBySection(sectionId);
        StudentAllResponse studentAllResponse = studentService.getAllStudentsBySection(sectionId);
        SectionTaskAllResponse sectionTaskAllResponse = taskService.readTaskBySection(sectionId);
        return new SectionDetailResponse(sectionResponse, lectureAllResponse, studentAllResponse, sectionTaskAllResponse);
    }

    // 반 삭제
    @Transactional
    public void deleteSection(Long sectionId) {
        Section section = sectionSubService.findSectionBySectionId(sectionId);
        sectionRepository.delete(section);
    }

    // 반 정보 수정
    @Transactional
    public SectionResponse updateSection(SectionUpdateRequest request) {

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new MyException(ErrorCode.SECTION_NOT_FOUND));
        section.update(request.getName(), request.getTeacher(), request.getHomeRoom());
        log.info("update contetns: {} {} {}", section.getName(), section.getTeacher(), section.getHomeRoom());
        sectionRepository.save(section);

        return new SectionResponse(section);
    }

}
