package com.yangyoung.server.lecture.service;

import com.yangyoung.server.day.domain.Day;
import com.yangyoung.server.day.domain.DayRepository;
import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lecture.domain.LectureRepository;
import com.yangyoung.server.lecture.dto.request.LectureCreateRequest;
import com.yangyoung.server.lecture.dto.request.LectureSeqUpdateRequest;
import com.yangyoung.server.lecture.dto.request.LectureUpdateRequest;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.lecture.dto.response.LecturePostResponse;
import com.yangyoung.server.lecture.dto.response.LectureResponse;
import com.yangyoung.server.lectureDate.domain.LectureDate;
import com.yangyoung.server.lectureDate.domain.LectureDateRepository;
import com.yangyoung.server.lectureDay.domain.LectureDay;
import com.yangyoung.server.lectureDay.domain.LectureDayRepository;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.service.SectionSubService;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import com.yangyoung.server.sectionLecture.domain.SectionLectureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final DayRepository dayRepository;
    private final LectureDayRepository lectureDayRepository;
    private final LectureDateRepository lectureDateRepository;
    private final SectionLectureRepository sectionLectureRepository;

    private final LectureSubService lectureSubService;
    private final SectionSubService sectionSubService;

    // 강의 등록(이름 중복 확인 + 정보 생성 + 요일 할당 + 반 할당)
    @Transactional
    public LecturePostResponse createLecture(LectureCreateRequest request) {

        isExistLectureByName(request.getName());
        Lecture lecture = lectureRepository.save(request.toEntity());
        lecture.updateLectureSeq(lecture.getId());
        assignDayToLecture(lecture, request.getDayList());
        assignDateToLecture(lecture, request.getDateList());
        List<String> sectionName = assignLectureToSection(request.getSectionIdList(), lecture);

        return new LecturePostResponse(lecture, sectionName);
    }

    // 강의명 중복 검사
    private void isExistLectureByName(String name) {
        Optional<Lecture> lecture = lectureRepository.findByName(name);
        if (lecture.isPresent()) { // 이미 존재하는 강의명이라면
            throw new MyException(ErrorCode.LECTURE_ALREADY_EXIST);
        }
    }

    // 강의 요일 할당
    private void assignDayToLecture(Lecture lecture, List<String> dayList) {

        if (dayList.isEmpty()) {
            return;
        }
        List<LectureDay> lectureDayList = dayList.stream()
                .map(dayName -> {
                    Day day = dayRepository.findByDayName(dayName);
                    return LectureDay.builder().day(day).lecture(lecture).build();
                })
                .collect(Collectors.toList());
        lectureDayRepository.saveAll(lectureDayList);
    }

    // 강의 날짜 할당
    private void assignDateToLecture(Lecture lecture, List<LocalDate> dateList) {

        List<LectureDate> lectureDateList = dateList.stream()
                .map(date -> LectureDate.builder().date(date).lecture(lecture).build())
                .collect(Collectors.toList());
        lectureDateRepository.saveAll(lectureDateList);
    }

    // 반 할당
    private List<String> assignLectureToSection(List<Long> sectionId, Lecture lecture) {

        List<Section> section = sectionSubService.findSectionsBySectionIdList(sectionId);
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

    // 전체 강의 조회
    @Transactional
    public LectureAllResponse getAllLectures() {

        List<Lecture> lectureList = lectureRepository.findAll();
        List<LectureResponse> lectureResponseList
                = lectureList.stream()
                .sorted(Comparator.comparing(Lecture::getLectureSeq).reversed())
                .map(LectureResponse::new)
                .collect(Collectors.toList());

        return new LectureAllResponse(lectureResponseList, lectureResponseList.size());
    }

    // 강의 리스트 순서 변경
    @Transactional
    public void updateLectureSeq(LectureSeqUpdateRequest request) {

        request.getLectureSeqList()
                .forEach(map -> {
                    Long lectureId = map.getId();
                    Long seq = map.getLectureSeq();

                    Lecture lecture = findLectureByLectureId(lectureId);
                    if (lecture != null) {
                        lecture.updateLectureSeq(seq);
                    } else {
                        throw new IllegalArgumentException("Lecture not found for ID: " + lectureId);
                    }
                });
    }

    // 반 별 강의 조회
    @Transactional
    public LectureAllResponse getLecturesBySection(Long sectionId) {
        List<Lecture> lectureList = lectureSubService.getLecturesBySection(sectionId);

        return lectureSubService.buildLectureAllResponse(lectureList);
    }

    // id로 강의 조회(수업 정보 + 반 이름)
    public LectureResponse getOneLecture(Long lectureId) {
        Lecture lecture = findLectureByLectureId(lectureId);
        return new LectureResponse(lecture);
    }


    // 강의 정보 수정
    @Transactional
    public void updateLecture(LectureUpdateRequest request) {
        Lecture lecture = findLectureByLectureId(request.getId());
        lecture.update(request.getName(), request.getTeacher(), request.getStartTime(), request.getEndTime(), request.getLectureRoom());
        updateLectureDay(request.getId(), request.getDayList());
        updateLectureDate(request.getId(), request.getDateList());
    }

    // 강의 날짜(Day) 수정
    public void updateLectureDay(Long lectureId, List<String> dayList) {
        Lecture lecture = findLectureByLectureId(lectureId);
        List<LectureDay> lectureDayList = lectureDayRepository.findByLectureId(lectureId);
        lectureDayRepository.deleteAll(lectureDayList);

        assignDayToLecture(lecture, dayList);
    }

    // 강의 날짜(Date) 수정
    public void updateLectureDate(Long lectureId, List<LocalDate> dateList) {
        Lecture lecture = findLectureByLectureId(lectureId);
        List<LectureDate> lectureDateList = lectureDateRepository.findByLectureId(lectureId);
        lectureDateRepository.deleteAll(lectureDateList);

        assignDateToLecture(lecture, dateList);
    }

    // 강의 삭제
    @Transactional
    public void deleteLecture(Long lectureId) {
        log.info("lectureId: {}", lectureId);
        Lecture lecture = findLectureByLectureId(lectureId);
        lectureRepository.delete(lecture);

        List<SectionLecture> sectionLectureList = sectionLectureRepository.findByLectureId(lectureId);
        sectionLectureRepository.deleteAll(sectionLectureList);
    }

    // id로 강의 존재 확인
    private Lecture findLectureByLectureId(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new MyException(ErrorCode.LECTURE_NOT_FOUND));
    }
}
