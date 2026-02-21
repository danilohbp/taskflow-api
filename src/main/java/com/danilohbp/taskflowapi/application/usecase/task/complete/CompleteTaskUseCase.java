package com.danilohbp.taskflowapi.application.usecase.task.complete;

import com.danilohbp.taskflowapi.application.service.TaskPermission;
import com.danilohbp.taskflowapi.domain.exception.BusinessRuleException;
import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.domain.model.Task;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.TaskRepository;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompleteTaskUseCase {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CompleteTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(CompleteTaskCommand cmd) {
        Task task = taskRepository.findById(cmd.taskId())
                .orElseThrow(() -> new NotFoundException("Task not found: " + cmd.taskId()));

        User actor = userRepository.findById(cmd.actorUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + cmd.actorUserId()));

        if (!TaskPermission.canComplete(actor, task)) {
            throw new BusinessRuleException("User cannot complete this task");
        }

        task.complete();
        taskRepository.save(task);
    }
}
