package com.mario.task.mapper;

import com.mario.task.domain.CreateTaskRequest;
import com.mario.task.domain.UpdateTaskRequest;
import com.mario.task.domain.dto.CreateTaskRequestDto;
import com.mario.task.domain.dto.TaskDto;
import com.mario.task.domain.dto.UpdateTaskRequestDto;
import com.mario.task.domain.entity.Task;

public interface TaskMapper {

    CreateTaskRequest fromDto(CreateTaskRequestDto dto);

    TaskDto toDto(Task task);
    UpdateTaskRequest fromDto(UpdateTaskRequestDto dto);
}
