package com.yangyoung.server.task.service;

import com.yangyoung.server.task.domain.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskSubService {

    private final TaskRepository taskRepository;
}
