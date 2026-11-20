package com.mario.task.exception;

import java.time.LocalDate;

public class InvalidDueDateException extends RuntimeException {

    public InvalidDueDateException(LocalDate dueDate) {
        super(String.format("Due date '%s' must be today or in the future.", dueDate));
    }
}