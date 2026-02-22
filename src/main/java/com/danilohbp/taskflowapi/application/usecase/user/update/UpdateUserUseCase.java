package com.danilohbp.taskflowapi.application.usecase.user.update;

import com.danilohbp.taskflowapi.domain.exception.BusinessRuleException;
import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UpdateUserUseCase {

    private final UserRepository userRepository;

    public UpdateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UpdateUserResult execute(UpdateUserCommand cmd) {

        if (cmd == null) {
            throw new IllegalArgumentException("UpdateUserCommand is required");
        }

        User user = userRepository.findById(cmd.userId())
                .orElseThrow(() ->
                        new NotFoundException("User not found: " + cmd.userId())
                );

        // 🔹 Atualização parcial
        if (cmd.name() != null) {
            user.changeName(cmd.name()); // validação no domínio
        }

        if (cmd.email() != null) {
            String normalizedEmail = cmd.email().trim().toLowerCase();

            // Só valida duplicidade se realmente mudou
            if (!Objects.equals(user.getEmail(), normalizedEmail)) {
                if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
                    throw new BusinessRuleException("Email already in use");
                }
                user.changeEmail(cmd.email()); // domínio normaliza/valida
            }
        }

        User saved = userRepository.save(user);

        return new UpdateUserResult(saved.getId());
    }
}
