package com.GoalLineNews.controller.Backend;

import com.GoalLineNews.dto.NewsDTO;
import com.GoalLineNews.dto.TagDTO;
import com.GoalLineNews.dto.UserDTO;
import com.GoalLineNews.service.NewsService;
import com.GoalLineNews.service.TagService;
import com.GoalLineNews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String adminPage() {
        return "admin/home";
    }

    @GetMapping("/news")
    public String newsAdmin(Model model) {
        List<NewsDTO> allNews = newsService.getAllNews();
        model.addAttribute("allNews", allNews);
        return "admin/news";
    }

    @GetMapping("/tags")
    public String tagsAdmin(Model model) {
        List<TagDTO> allTags = tagService.getAllTags();
        model.addAttribute("allTags", allTags);
        return "admin/tag/tag";
    }

    @GetMapping("/users")
    public String usersAdmin(Model model) {
        List<UserDTO> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "admin/user/user";
    }
}
