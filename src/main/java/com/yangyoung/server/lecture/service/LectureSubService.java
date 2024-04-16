package com.yangyoung.server.lecture.service;

import com.yangyoung.server.day.domain.Day;
import com.yangyoung.server.day.domain.DayRepository;
import com.yangyoung.server.exception.lecture.LectureNotFoundException;
import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lecture.domain.LectureRepository;
import com.yangyoung.server.lecture.dto.request.LectureCreateRequest;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LectureBriefResponse;
import com.yangyoung.server.lectureDate.domain.LectureDate;
import com.yangyoung.server.lectureDate.domain.LectureDateRepository;
import com.yangyoung.server.lectureDay.domain.LectureDay;
import com.yangyoung.server.lectureDay.domain.LectureDayRepository;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionUtilService;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import com.yangyoung.server.sectionLecture.domain.SectionLectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureSubService {

    private final LectureRepository lectureRepository;
    private final LectureDayRepository lectureDayRepository;
    private final SectionUtilService sectionUtilService;
    private final SectionLectureRepository sectionLectureRepository;
    private final DayRepository dayRepository;
    private final LectureDateRepository lectureDateRepository;

    private final Logger logger = LoggerFactory.getLogger(LectureSubService.class);

    // 강의명으로 강의 존재 확인
    public void isExistLectureByName(String name) {
        Optional<Lecture> lecture = lectureRepository.findByName(name);
        if (lecture.isPresent()) { // 이미 존재하는 강의명이라면
            String message = String.format("Lecture name already exists: %s", name);
            logger.info(message);
            throw new LectureNotFoundException(message);
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

    // 강의 날짜 할당
    public void assignDateToLecture(Lecture lecture, List<LocalDate> dateList) {

        List<LectureDate> lectureDateList = dateList.stream()
                .map(date -> LectureDate.builder().date(date).lecture(lecture).build())
                .collect(Collectors.toList());
        lectureDateRepository.saveAll(lectureDateList);

        lecture.updateLectureDate(lectureDateList);
        lectureRepository.save(lecture);
    }

    // 반 할당
    public List<String> assignLectureToSection(List<Long> sectionId, Lecture lecture) {

        List<Section> section = sectionUtilService.findSectionsBySectionIdList(sectionId);
        List<SectionLecture> sectionLectureList = section.stream()
                .map(section1 -> SectionLecture.builder()
                        .section(section1)
                        .lecture(lecture)
                        .build())
                .toList();
        sectionLectureRepository.saveAll(sectionLectureList);

        return section.stream()
                .map(Section::getName)
                .collect(Collectors.toList());
    }

    // 반 별 강의 엔티티 조회
//    public List<Lecture> getLecturesBySection(Long sectionId) {
//        return sectionLectureRepository.findBySectionId(sectionId).stream()
//                .map(sectionLecture -> lectureRepository.findById(sectionLecture.getLecture().getId())
//                        .orElseThrow(() -> new MyException(ErrorCode.LECTURE_NOT_FOUND)))
//                .collect(Collectors.toList());
//    }
    // 반 별 강의 엔티티 조회
    public List<Lecture> getLecturesBySection(Long sectionId) {
        return sectionLectureRepository.findBySectionId(sectionId).stream()
                .map(SectionLecture::getLecture)
                .collect(Collectors.toList());
    }

    // 반 별 강의 응답 생성
    public LectureAllResponse buildLectureAllResponse(List<Lecture> lectureList) {
        List<LectureBriefResponse> lectureResponseList = lectureList.stream()
                .map(LectureBriefResponse::new)
                .collect(Collectors.toList());

        return new LectureAllResponse(lectureResponseList, lectureResponseList.size());
    }
}
