package com.danilohbp.taskflowapi.application.usecase.user.deactivate;

import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeactivateUserUseCase {

    private final UserRepository userRepository;

    public DeactivateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(DeactivateUserCommand cmd) {

        if (cmd == null) {
            throw new IllegalArgumentException("DeactivateUserCommand is required");
        }

        User user = userRepository.findById(cmd.userId())
                .orElseThrow(() ->
                        new NotFoundException("User not found: " + cmd.userId())
                );

        // Idempotente: se já estiver desativado, não faz nada
        if (user.isActive()) {
            user.deactivate();
            userRepository.save(user);
        }
    }
}
