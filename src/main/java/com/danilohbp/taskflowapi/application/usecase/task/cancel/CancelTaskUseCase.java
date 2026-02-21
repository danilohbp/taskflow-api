package com.danilohbp.taskflowapi.application.usecase.task.cancel;

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
public class CancelTaskUseCase {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CancelTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(CancelTaskCommand cmd) {
        if (cmd == null) {
            throw new IllegalArgumentException("CancelTaskCommand is required");
        }
        if (cmd.actorUserId() == null) {
            throw new IllegalArgumentException("actorUserId is required");
        }
        if (cmd.taskId() == null) {
            throw new IllegalArgumentException("taskId is required");
        }

        Task task = taskRepository.findById(cmd.taskId())
                .orElseThrow(() -> new NotFoundException("Task not found: " + cmd.taskId()));

        User actor = userRepository.findById(cmd.actorUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + cmd.actorUserId()));

        if (!TaskPermission.canCancel(actor, task)) {
            throw new BusinessRuleException("User cannot cancel this task");
        }

        // Se você quiser usar "reason", crie task.cancel(cmd.reason()) no domínio
        // Por enquanto, cancel simples:
        task.cancel();

        taskRepository.save(task);
    }
}
