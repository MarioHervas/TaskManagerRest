package com.mario.task.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mario.task.domain.CreateTaskRequest;
import com.mario.task.domain.UpdateTaskRequest;
import com.mario.task.domain.entity.Task;
import com.mario.task.domain.entity.TaskPriority;
import com.mario.task.domain.entity.TaskStatus;
import com.mario.task.exception.TaskNotFoundException;
import com.mario.task.repository.TaskRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.mario.task.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTaskSetsStatusOpenAndEqualTimestamps() {
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CreateTaskRequest request = new CreateTaskRequest(
                "Title", "Description", LocalDate.now().plusDays(1), TaskPriority.HIGH);

        Task task = taskService.createTask(request);

        assertEquals("Title", task.getTitle());
        assertEquals(TaskStatus.OPEN, task.getStatus());
        assertEquals(TaskPriority.HIGH, task.getPriority());
        assertNotNull(task.getCreated());
        assertEquals(task.getCreated(), task.getUpdated());
    }

    @Test
    void listTasksSortsByCreatedAscending() {
        taskService.listTasks();

        verify(taskRepository).findAll(Sort.by(Direction.ASC, "created"));
    }

    @Test
    void updateTaskUpdatesFieldsAndKeepsCreated() {
        UUID id = UUID.randomUUID();
        Instant created = Instant.parse("2026-01-01T00:00:00Z");
        Task existing = new Task(
                id, "Old", "Old description", null, TaskStatus.OPEN, TaskPriority.LOW, created, created);
        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UpdateTaskRequest request = new UpdateTaskRequest(
                "New", "New description", null, TaskStatus.COMPLETE, TaskPriority.HIGH);

        Task updated = taskService.updateTask(id, request);

        assertEquals("New", updated.getTitle());
        assertEquals("New description", updated.getDescription());
        assertEquals(TaskStatus.COMPLETE, updated.getStatus());
        assertEquals(TaskPriority.HIGH, updated.getPriority());
        assertEquals(created, updated.getCreated());
        assertTrue(updated.getUpdated().isAfter(created));
    }

    @Test
    void updateTaskThrowsWhenTaskDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        UpdateTaskRequest request = new UpdateTaskRequest(
                "Title", null, null, TaskStatus.COMPLETE, TaskPriority.LOW);

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(id, request));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTaskDelegatesToRepository() {
        UUID id = UUID.randomUUID();

        taskService.deleteTask(id);

        verify(taskRepository).deleteById(id);
    }
}