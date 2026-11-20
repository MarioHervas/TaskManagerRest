package com.mario.task.domain;

import com.mario.task.domain.entity.TaskPriority;

import java.time.LocalDate;

public record CreateTaskRequest(String title, String description, LocalDate dueDate, TaskPriority priority) {

}
