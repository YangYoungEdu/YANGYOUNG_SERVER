package com.yangyoung.server.task.service;

import com.yangyoung.server.task.domain.Task;
import com.yangyoung.server.task.domain.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskSubService {

    private final TaskRepository taskRepository;

    private final Logger logger = LoggerFactory.getLogger(TaskSubService.class);

    @Transactional
    public Task findTaskByTaskId(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            String message = String.format("Task not found. (taskId: %d)", taskId);
            logger.info(message);
            throw new IllegalArgumentException(message);
        }
        return task.get();
    }
}
