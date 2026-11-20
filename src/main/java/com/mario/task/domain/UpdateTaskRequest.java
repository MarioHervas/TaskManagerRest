package com.mario.task.domain;

import com.mario.task.domain.entity.TaskPriority;
import com.mario.task.domain.entity.TaskStatus;

import java.time.LocalDate;

public record UpdateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus status,
        TaskPriority priority

) {
}
