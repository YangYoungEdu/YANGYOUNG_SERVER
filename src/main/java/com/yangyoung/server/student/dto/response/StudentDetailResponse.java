package com.yangyoung.server.student.dto.response;

import com.yangyoung.server.lecture.dto.response.LectureAllResponse;
import com.yangyoung.server.studentTask.dto.response.StudentTaskAllResponse;
import com.yangyoung.server.task.dto.response.TaskAllResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailResponse {

    private StudentResponse studentResponse;

    private LectureAllResponse lectureAllResponse;

    private StudentTaskAllResponse studentTaskAllResponse;

}
