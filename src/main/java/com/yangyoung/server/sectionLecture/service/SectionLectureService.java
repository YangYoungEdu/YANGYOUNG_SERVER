package com.yangyoung.server.sectionLecture.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.lecture.domain.Lecture;
import com.yangyoung.server.lecture.domain.LectureRepository;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.sectionLecture.domain.SectionLecture;
import com.yangyoung.server.sectionLecture.domain.SectionLectureRepository;
import com.yangyoung.server.sectionLecture.dto.request.EnrollLectureCreateRequest;
import com.yangyoung.server.sectionLecture.dto.response.EnrollLecturePostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionLectureService {

    private final SectionLectureRepository sectionlectureRepository;
    private final SectionRepository sectionRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public EnrollLecturePostResponse enrollLecture(EnrollLectureCreateRequest request) {

        Optional<Section> section = sectionRepository.findById(request.getLectureId());
        if (section.isEmpty()) {
            throw new MyException(ErrorCode.SECTION_NOT_FOUND);
        }

        Optional<Lecture> lecture = lectureRepository.findById(request.getLectureId());
        if (lecture.isEmpty()) {
            throw new MyException(ErrorCode.LECTURE_NOT_FOUND);
        }

        SectionLecture sectionLecture = new SectionLecture(section.get(), lecture.get());
        sectionlectureRepository.save(sectionLecture);

        return new EnrollLecturePostResponse(
                section.get(), lecture.get());

    }
}
