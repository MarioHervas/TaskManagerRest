package com.mario.task.service;

import com.mario.task.domain.CreateTaskRequest;
import com.mario.task.domain.UpdateTaskRequest;
import com.mario.task.domain.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    Task createTask(CreateTaskRequest request);
    List<Task> listTasks();
    Task updateTask(UUID taskId, UpdateTaskRequest request);

    void deleteTask(UUID taskId);
}
