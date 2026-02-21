package com.danilohbp.taskflowapi.application.service;

import com.danilohbp.taskflowapi.application.usecase.task.CreateTaskRequest;
import com.danilohbp.taskflowapi.application.usecase.task.TaskResponse;
import com.danilohbp.taskflowapi.application.usecase.task.UpdateTaskRequest;
import com.danilohbp.taskflowapi.domain.exception.BusinessRuleException;
import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.domain.model.Task;
import com.danilohbp.taskflowapi.domain.model.TaskPriority;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.TaskRepository;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public TaskResponse create(CreateTaskRequest request) {

        if (request.userId() == null) {
            throw new BusinessRuleException("UserId é obrigatório.");
        }

        if (request.title() == null || request.title().isBlank()) {
            throw new BusinessRuleException("Title é obrigatório.");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado para criar tarefa."));

        Task task = new Task(user, request.title(), TaskPriority.LOW, LocalDateTime.now());

        Task saved = taskRepository.save(task);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));
        return toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateById(Long id, UpdateTaskRequest request) {
        if (request == null) {
            throw new BusinessRuleException("Body da requisição é obrigatório.");
        }

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));

        task.updateDetails(request.title(), request.description(), TaskPriority.LOW, LocalDateTime.now());

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Override
    public List<TaskResponse> listAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private static <T> void applyIfPresent(T value, java.util.function.Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getAssignee().getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus()
        );
    }
}
