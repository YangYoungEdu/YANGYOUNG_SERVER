package com.yangyoung.server.lecture.service;

import com.yangyoung.server.day.domain.Day;
import com.yangyoung.server.day.domain.DayRepository;
import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lecture.domain.LectureRepository;
import com.yangyoung.server.lecture.dto.request.LectureCreateRequest;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureResponse;
import com.yangyoung.server.lectureDay.domain.LectureDay;
import com.yangyoung.server.lectureDay.domain.LectureDayRepository;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import com.yangyoung.server.sectionLecture.domain.SectionLectureRepository;
import com.yangyoung.server.util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureSubService {

    private final LectureRepository lectureRepository;
    private final LectureDayRepository lectureDayRepository;
    private final SectionSubService sectionSubService;
    private final SectionLectureRepository sectionLectureRepository;
    private final DayRepository dayRepository;
    private final UtilService utilService;

    // 강의명으로 강의 존재 확인
    public void isExistLectureByName(String name) {
        Optional<Lecture> lecture = lectureRepository.findByName(name);
        if (lecture.isPresent()) { // 이미 존재하는 강의명이라면
            throw new MyException(ErrorCode.LECTURE_ALREADY_EXIST);
        }
    }

    // 강의 정보 생성
    public Lecture createLectureInfo(LectureCreateRequest request) {
        return lectureRepository.save(request.toEntity());
    }

    // 강의 요일 할당
    public void assignDayToLecture(Lecture lecture, List<String> dayList) {

        List<LectureDay> lectureDayList = dayList.stream()
                .map(dayName -> {
                    Day day = dayRepository.findByDayName(dayName);
                    return LectureDay.builder().day(day).lecture(lecture).build();
                })
                .collect(Collectors.toList());
        lectureDayRepository.saveAll(lectureDayList);

        lecture.updateLectureDay(lectureDayList);
        lectureRepository.save(lecture);
    }

    // 반 할당
    public String assignLectureToSection(Long sectionId, Lecture lecture) {

        Section section = sectionSubService.isSectionExist(sectionId);

        SectionLecture sectionLecture = SectionLecture.builder()
                .section(section)
                .lecture(lecture)
                .build();
        sectionLectureRepository.save(sectionLecture);

        return section.getName();
    }

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

    // 오늘 강의 조회
    public List<LectureResponse> getTodayLectures(LectureAllResponse lectureAllResponse, DayOfWeek todayDayOfWeek) {

        String today = utilService.convertDayToKorean(todayDayOfWeek.toString());

        return lectureAllResponse.getLectureResponseList().stream()
                .filter(lecture -> lecture.getDayList().contains(today))
                .toList();
    }
}
