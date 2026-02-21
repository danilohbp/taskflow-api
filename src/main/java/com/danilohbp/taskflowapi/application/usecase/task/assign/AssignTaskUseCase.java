package com.danilohbp.taskflowapi.application.usecase.task.assign;

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
public class AssignTaskUseCase {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public AssignTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(AssignTaskCommand cmd) {
        if (cmd == null) {
            throw new IllegalArgumentException("AssignTaskCommand is required");
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

        if (!TaskPermission.canAssign(actor, task)) {
            throw new BusinessRuleException("User cannot assign this task");
        }

        User newAssignee = null;
        if (cmd.assigneeId() != null) {
            newAssignee = userRepository.findById(cmd.assigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found: " + cmd.assigneeId()));
        }

        // Domínio decide como atribuir (pode ser null para "unassign")
        task.assignTo(newAssignee);

        // Em transação, o dirty-checking já atualiza; salvar explícito é ok
        taskRepository.save(task);
    }
}
