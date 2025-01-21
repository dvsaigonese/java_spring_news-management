package com.GoalLineNews.controller;

import com.GoalLineNews.dto.UserDTO;
import com.GoalLineNews.entity.User;
import com.GoalLineNews.service.UserService;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}")
    public String updateUser(HttpServletRequest request, @PathVariable int id, UserDTO userDTO) {
        String referer = request.getHeader("Referer");
        try {
            userService.updateUser(id, userDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer  + "?error";
        }
        return "redirect:" + referer  + "?success";
    }

    @PostMapping("/create")
    public String addUser(@RequestBody User user) {
        try {
            System.out.println("Received request to create user: " + user);
            userService.addUser(user);
            return "Thành công";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi: " + e.getMessage();
        }
    }
}
