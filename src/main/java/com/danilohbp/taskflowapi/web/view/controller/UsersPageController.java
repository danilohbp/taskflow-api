package com.danilohbp.taskflowapi.web.view.controller;

import com.danilohbp.taskflowapi.infrastructure.persistence.query.UserQueryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersPageController {

    private final UserQueryRepository userQueryRepository;

    public UsersPageController(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userQueryRepository.listUsers());
        return "users";
    }
}
