package com.GoalLineNews.controller.Backend;

import com.GoalLineNews.dto.UserDTO;
import com.GoalLineNews.entity.User;
import com.GoalLineNews.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "admin/user/create";
    }

    @GetMapping("/edit/{id}")
    public String getUserById(@PathVariable("id") int id, Model model) {
        UserDTO userDTO = userService.getUserById(id);
        model.addAttribute("user", userDTO);
        return "admin/user/edit";
    }

    @PostMapping("/store")
    public String store(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        String referer = request.getHeader("Referer");
        try {
            userService.createUser(userDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer + "?error";
        }
        return "redirect:" + referer + "?success";
    }

    @PostMapping("/update/{id}")
    public String update(HttpServletRequest request, @PathVariable("id") int id, @ModelAttribute("user") UserDTO userDTO) {
        String referer = request.getHeader("Referer"); // Lấy URL trước đó để điều hướng lại
        try {
            userService.updateUser(id, userDTO); // Gọi service để cập nhật người dùng
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer + "?error"; // Điều hướng lại nếu xảy ra lỗi
        }
        return "redirect:" + referer + "?success"; // Điều hướng lại với thông báo thành công
    }

    @GetMapping("/delete/{id}")
    public String destroy(HttpServletRequest request, @PathVariable("id") int id) {
        String referer = request.getHeader("Referer");
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer + "?error";
        }
        return "redirect:" + referer + "?success";
    }

}
