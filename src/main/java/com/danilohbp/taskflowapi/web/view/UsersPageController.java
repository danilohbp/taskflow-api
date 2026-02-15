package com.danilohbp.taskflowapi.web.view;

import com.danilohbp.taskflowapi.application.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersPageController {

    private final UserService userService;

    public UsersPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.listAll());
        return "users";
    }
}
