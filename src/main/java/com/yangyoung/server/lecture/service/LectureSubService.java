package com.yangyoung.server.lecture.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lecture.domain.LectureRepository;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureResponse;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import com.yangyoung.server.sectionLecture.domain.SectionLectureRepository;
import com.yangyoung.server.util.UtilService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureSubService {

    private final LectureRepository lectureRepository;
    private final SectionSubService sectionSubService;
    private final SectionLectureRepository sectionLectureRepository;
    private final UtilService utilService;

    // 반 별 강의 엔티티 조회
    public List<Lecture> getLecturesBySection(Long sectionId) {
        return sectionLectureRepository.findBySectionId(sectionId).stream()
                .map(sectionLecture -> lectureRepository.findById(sectionLecture.getLecture().getId())
                        .orElseThrow(() -> new MyException(ErrorCode.LECTURE_NOT_FOUND)))
                .collect(Collectors.toList());
    }

    // 반 별 강의 응답 생성
    public LectureAllResponse buildLectureAllResponse(List<Lecture> lectureList) {

        List<LectureResponse> lectureResponseList = lectureList.stream()
                .map(LectureResponse::new)
                .collect(Collectors.toList());

        return new LectureAllResponse(lectureResponseList, lectureResponseList.size());
    }

    // 학생이 속한 분반 목록 & 수업에 속한 분반 목록
    // 오늘 강의 조회
    public LectureAllResponse getTodayLectures(LectureAllResponse lectureAllResponse, DayOfWeek todayDayOfWeek, LocalDate today) {

        String todayString = utilService.convertDayToKorean(todayDayOfWeek.toString());

        List<LectureResponse> lectureResponseList = lectureAllResponse.getLectureResponseList().stream()
                .filter(lecture -> lecture.getDayList().contains(todayString) || lecture.getDateList().contains(today))
                .sorted(Comparator.comparing(LectureResponse::getStartTime))
                .toList();

        return new LectureAllResponse(lectureResponseList, lectureResponseList.size());
    }

    // 학생별 강의 조회
    @Transactional
    public LectureAllResponse getLecturesByStudent(Long studentId) {

        List<Section> sectionList = sectionSubService.findSectionsByStudentId(studentId);
        List<Lecture> lectureList = sectionList.stream()
                .map(section -> getLecturesBySection(section.getId()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return buildLectureAllResponse(lectureList);
    }
}
