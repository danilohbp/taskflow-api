package com.danilohbp.taskflowapi.application.service;

import com.danilohbp.taskflowapi.application.dto.task.CreateTaskRequest;
import com.danilohbp.taskflowapi.application.dto.task.TaskResponse;
import com.danilohbp.taskflowapi.application.dto.task.UpdateTaskRequest;
import com.danilohbp.taskflowapi.domain.exception.BusinessRuleException;
import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.domain.model.Task;
import com.danilohbp.taskflowapi.domain.model.TaskPriority;
import com.danilohbp.taskflowapi.domain.model.TaskStatus;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.domain.repository.TaskRepository;
import com.danilohbp.taskflowapi.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Task task = new Task();
        task.setUser(user);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.ABERTO);
        task.setPriority(TaskPriority.NIVEL_1);

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

        applyIfPresent(request.title(), task::setTitle);
        applyIfPresent(request.description(), task::setDescription);
        applyIfPresent(request.status(), task::setStatus);

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
            task.getUser().getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus()
        );
    }
}
