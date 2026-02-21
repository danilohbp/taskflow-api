package com.danilohbp.taskflowapi.application.usecase.user.create;

import com.danilohbp.taskflowapi.domain.exception.BusinessRuleException;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUserUseCase {

    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateUserResult execute(CreateUserCommand cmd) {
        if (cmd == null) throw new IllegalArgumentException("CreateUserCommand is required");

        // regra de negócio: email único
        if (userRepository.existsByEmailIgnoreCase(cmd.email())) {
            throw new BusinessRuleException("Email already in use");
        }

        User user = new User(cmd.name(), cmd.email(), cmd.role());
        User saved = userRepository.save(user);

        return new CreateUserResult(saved.getId());
    }
}
