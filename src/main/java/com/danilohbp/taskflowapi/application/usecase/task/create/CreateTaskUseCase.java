package com.danilohbp.taskflowapi.application.usecase.task.create;

import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.domain.model.Task;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.TaskRepository;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTaskUseCase {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CreateTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateTaskResult execute(CreateTaskCommand cmd) {
        Long reporterId = (cmd.reporterId() != null ? cmd.reporterId() : cmd.actorUserId());

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new NotFoundException("Reporter not found " + reporterId));

        User assignee = null;
        if (cmd.assigneeId() != null) {
            assignee = userRepository.findById(cmd.assigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found: " + cmd.assigneeId()));
        }

        Task task = new Task(reporter, cmd.title(), cmd.priority(), cmd.dueDate());
        task.updateDetails(cmd.title(), cmd.description(), cmd.priority(), cmd.dueDate());
        task.assignTo(assignee);

        Task saved = taskRepository.save(task);
        return new CreateTaskResult(saved.getId());
    }

}
