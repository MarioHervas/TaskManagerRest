package com.mario.task.service.impl;

import com.mario.task.domain.CreateTaskRequest;
import com.mario.task.domain.UpdateTaskRequest;
import com.mario.task.domain.entity.Task;
import com.mario.task.domain.entity.TaskStatus;
import com.mario.task.exception.TaskNotFoundException;
import com.mario.task.repository.TaskRepository;
import com.mario.task.service.TaskService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.mario.task.exception.InvalidDueDateException;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }
    @Override
    @Transactional
    public Task createTask(CreateTaskRequest request) {
        Instant now = Instant.now();
        Task task = new Task(null, request.title(), request.description(),request.dueDate(), TaskStatus.OPEN,request.priority(),now,now);

        return taskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> listTasks() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "created"));
    }

    @Override
    @Transactional
    public Task updateTask(UUID taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        boolean dueDateChanged = !Objects.equals(request.dueDate(), task.getDueDate());
        if (dueDateChanged && request.dueDate() != null
                && request.dueDate().isBefore(LocalDate.now())) {
            throw new InvalidDueDateException(request.dueDate());
        }

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setUpdated(Instant.now());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }
}
