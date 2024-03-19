package com.yangyoung.server.task.dto.response;

import com.yangyoung.server.sectionTask.dto.response.SectionTaskResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAllResponse {

    private List<StudentTaskResponse> studentTaskResponseList;

    private Integer studentTaskSize;

    private List<SectionTaskResponse> sectionTaskResponseList;

    private Integer sectionTaskSize;
}
