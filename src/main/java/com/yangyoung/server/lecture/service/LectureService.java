package com.yangyoung.server.lecture.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lecture.domain.LectureRepository;
import com.yangyoung.server.lecture.dto.request.LectureCreateRequest;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureResponse;
import com.yangyoung.server.lecture.dto.response.LecturePostResponse;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import com.yangyoung.server.sectionLecture.domain.SectionLectureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final SectionLectureRepository sectionLectureRepository;

    private final LectureSubService lectureSubService;
    private final SectionSubService sectionSubService;

    // 강의 등록(이름 중복 확인 + 정보 생성 + 요일 할당 + 반 할당)
    @Transactional
    public LecturePostResponse createLecture(LectureCreateRequest request) {

        lectureSubService.isExistLectureByName(request.getName());
        Lecture lecture = lectureSubService.createLectureInfo(request);
        lectureSubService.assignDayToLecture(lecture, request.getDayList());
        lectureSubService.assignDateToLecture(lecture, request.getDateList());
        List<String> sectionName = lectureSubService.assignLectureToSection(request.getSectionIdList(), lecture);

        return new LecturePostResponse(lecture, sectionName);
    }

    // 전체 강의 조회
    @Transactional
    public LectureAllResponse getAllLectures() {

        List<Lecture> lectureList = lectureRepository.findAll();
        List<LectureResponse> lectureResponseList
                = lectureList.stream()
                .map(LectureResponse::new)
                .collect(Collectors.toList());

        return new LectureAllResponse(lectureResponseList, lectureResponseList.size());
    }

    // 반 별 강의 조회
    @Transactional
    public LectureAllResponse getLecturesBySection(Long sectionId) {
        List<Lecture> lectureList = lectureSubService.getLecturesBySection(sectionId);

        return lectureSubService.buildLectureAllResponse(lectureList);
    }

    // id로 강의 조회(수업 정보 + 반 이름)
    public LectureResponse getOneLecture(Long lectureId) {
        Lecture lecture = isExistLectureById(lectureId);
        return new LectureResponse(lecture);
    }

    // 강의 삭제
    public void deleteLecture(Long lectureId) {

        Lecture lecture = isExistLectureById(lectureId);
        lectureRepository.delete(lecture);

        List<SectionLecture> sectionLectureList = sectionLectureRepository.findByLectureId(lectureId);
        sectionLectureRepository.deleteAll(sectionLectureList);
    }

    // id로 강의 존재 확인
    private Lecture isExistLectureById(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new MyException(ErrorCode.LECTURE_NOT_FOUND));
    }
}
