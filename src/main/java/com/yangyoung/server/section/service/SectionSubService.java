package com.yangyoung.server.section.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.dto.response.SectionAllBriefResponse;
import com.yangyoung.server.section.dto.response.SectionBriefResponse;
import com.yangyoung.server.section.dto.response.SectionResponse;
import com.yangyoung.server.studentSection.domain.StudentSection;
import com.yangyoung.server.studentSection.domain.StudentSectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionSubService {

    private final SectionRepository sectionRepository;
    private final StudentSectionRepository studentSectionRepository;


    // id에 해당하는 반 정보 조회 - 단일
    @Transactional
    public Section findSectionBySectionId(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new MyException(ErrorCode.SECTION_NOT_FOUND));
    }

    // id에 해당하는 반 정보 조회 - 다중
    @Transactional
    public List<Section> findSectionsBySectionIdList(List<Long> sectionIdList) {
        return sectionRepository.findAllById(sectionIdList);
    }

    // id에 해당하는 반 정보 조회
    @Transactional
    public SectionResponse readSectionInfo(Long sectionId) {

        Optional<Section> section = sectionRepository.findById(sectionId);
        if (section.isEmpty()) { /* 해당하는 반이 없는 경우 */
            throw new MyException(ErrorCode.SECTION_NOT_FOUND);
        }

        return new SectionResponse(section.get());
    }

    // 학생이 속한 반의 정보 조회
    @Transactional
    public SectionAllBriefResponse findSectionsBriefInfo(Long studentId) {

        List<StudentSection> studentSectionList = studentSectionRepository.findAllByStudentId(studentId);
        List<SectionBriefResponse> sectionBriefResponseList = studentSectionList.stream()
                .map(studentSection -> new SectionBriefResponse(
                        studentSection.getSection()))
                .toList();

        return new SectionAllBriefResponse(sectionBriefResponseList, sectionBriefResponseList.size());
    }

    // 학생이 속한 반의 엔티티 조회
    @Transactional
    public List<Section> findSectionsByStudentId(Long studentId) {
        List<StudentSection> studentSectionList = studentSectionRepository.findAllByStudentId(studentId);
        return studentSectionList.stream()
                .map(StudentSection::getSection)
                .toList();
    }

    // 학생이 속한 반 이름 리스트 조회
    @Transactional
    public List<String> findSectionNamesByStudentId(Long studentId) {
        List<StudentSection> studentSectionList = studentSectionRepository.findAllByStudentId(studentId);
        return studentSectionList.stream()
                .map(studentSection -> studentSection.getSection().getName())
                .collect(Collectors.toList());
    }
}

