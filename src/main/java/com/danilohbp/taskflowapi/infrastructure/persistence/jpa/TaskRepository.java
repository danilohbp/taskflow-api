package com.danilohbp.taskflowapi.infrastructure.persistence.jpa;

import com.danilohbp.taskflowapi.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> { }
