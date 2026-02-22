package com.danilohbp.taskflowapi.application.usecase.user.activate;

import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivateUserUseCase {

    private final UserRepository userRepository;

    public ActivateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(ActivateUserCommand cmd) {
        if (cmd == null) throw new IllegalArgumentException("ActivateUserCommand is required");

        User user = userRepository.findById(cmd.userId())
                .orElseThrow(() -> new NotFoundException("User not found: " + cmd.userId()));

        if (!user.isActive()) {
            user.activate();
            userRepository.save(user);
        }
    }
}
