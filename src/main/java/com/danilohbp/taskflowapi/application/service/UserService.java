package com.danilohbp.taskflowapi.application.service;

import com.danilohbp.taskflowapi.application.usecase.user.CreateUserRequest;
import com.danilohbp.taskflowapi.application.usecase.user.UserResponse;
import com.danilohbp.taskflowapi.domain.exception.BusinessRuleException;
import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (repository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessRuleException("E-mail já cadastrado.");
        }

        User user = new User(request.name(), request.email(), null, null);
        User saved = repository.save(user);

        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    @Override
    public List<UserResponse> listAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado!"));

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }
}
