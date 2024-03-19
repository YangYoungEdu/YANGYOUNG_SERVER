package com.yangyoung.server.studentTask.dto.response;

import com.yangyoung.server.sectionTask.dto.response.SectionTaskResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentTaskAllResponse {

    private List<StudentTaskResponse> studentTaskResponseList;

    private Integer studentTaskSize;
}
