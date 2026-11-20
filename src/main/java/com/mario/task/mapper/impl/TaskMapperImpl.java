package com.mario.task.mapper.impl;

import com.mario.task.domain.CreateTaskRequest;
import com.mario.task.domain.UpdateTaskRequest;
import com.mario.task.domain.dto.CreateTaskRequestDto;
import com.mario.task.domain.dto.TaskDto;
import com.mario.task.domain.dto.UpdateTaskRequestDto;
import com.mario.task.domain.entity.Task;
import com.mario.task.mapper.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {
    @Override
    public CreateTaskRequest fromDto(CreateTaskRequestDto dto) {
        return new CreateTaskRequest(
                dto.title(), dto.description(), dto.dueDate(),dto.priority()
        );
    }

    @Override
    public TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus()

        );
    }

    @Override
    public UpdateTaskRequest fromDto(UpdateTaskRequestDto dto) {
        return new UpdateTaskRequest(
                dto.title(),
                dto.description(),
                dto.dueDate(),
                dto.status(),
                dto.priority()
        );
    }
}
