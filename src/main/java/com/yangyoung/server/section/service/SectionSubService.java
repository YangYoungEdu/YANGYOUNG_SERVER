package com.yangyoung.server.section.service;

import com.yangyoung.server.exception.ErrorCode;
import com.yangyoung.server.exception.MyException;
import com.yangyoung.server.section.domain.Section;
import com.yangyoung.server.section.domain.SectionRepository;
import com.yangyoung.server.section.dto.response.SectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionSubService {

    private final SectionRepository sectionRepository;

    public Section isSectionExist(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new MyException(ErrorCode.SECTION_NOT_FOUND));
    }

    // id에 해당하는 반 정보 조회
    public SectionResponse readSectionInfo(Long sectionId) {

        Optional<Section> section = sectionRepository.findById(sectionId);
        if (section.isEmpty()) { /* 해당하는 반이 없는 경우 */
            throw new MyException(ErrorCode.SECTION_NOT_FOUND);
        }

        return new SectionResponse(
                section.get().getId(),
                section.get().getName(),
                section.get().getTeacher());
    }
}

