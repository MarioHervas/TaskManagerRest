package com.mario.task.repository;

import com.mario.task.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {


}
