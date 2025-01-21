package com.GoalLineNews.controller;

import com.GoalLineNews.GoalLineNewsManagementApplication;
import com.GoalLineNews.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.GoalLineNews.entity.User;
import com.GoalLineNews.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(UserDTO userDTO) {
        userDTO.setRole(GoalLineNewsManagementApplication.Role.USER);
        userService.createUser(userDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


}