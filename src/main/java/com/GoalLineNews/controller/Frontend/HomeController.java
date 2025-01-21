package com.GoalLineNews.controller.Frontend;

import com.GoalLineNews.auth.UnifiedUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import com.GoalLineNews.dto.NewsDTO;
import com.GoalLineNews.dto.UserDTO;
import com.GoalLineNews.service.NewsService;
import com.GoalLineNews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        List<NewsDTO> top4news = newsService.getTop4NewsInHomePage();
        NewsDTO latestNews = newsService.getLatestNews();

        LocalDateTime now = LocalDateTime.now();
        for (NewsDTO news : top4news) {
            Duration duration = Duration.between(news.getTime(), now);
            news.setDisplayTime(formatTimeElapsed(duration)); // Set thời gian đã đăng
        }
        Duration lastNewsDuration = Duration.between(latestNews.getTime(), now);
        latestNews.setDisplayTime(formatTimeElapsed(lastNewsDuration));

        model.addAttribute("top4news", top4news);
        model.addAttribute("latestNews", latestNews);
        return "home";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(value = "query", required = false) String query,
            Model model) {
        List<NewsDTO> results = new ArrayList<>();
        if (query != null && !query.trim().isEmpty()) {
            results = newsService.searchNewsByKeyword(query.trim());
        }
        LocalDateTime now = LocalDateTime.now();
        for (NewsDTO news : results) {
            Duration duration = Duration.between(news.getTime(), now);
            news.setDisplayTime(formatTimeElapsed(duration)); // Set thời gian đã đăng
        }
        model.addAttribute("query", query);
        model.addAttribute("results", results);
        return "search";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UnifiedUserDetails userDetails, Model model) {
        int userId = userDetails.getDatabaseId();
        UserDTO userProfile = userService.getUserById(userId);
        model.addAttribute("userProfile", userProfile);
        return "profile";
    }

    private String formatTimeElapsed(Duration duration) {
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else if (days < 30) {
            return days + " days ago";
        } else if (days < 365) {
            long months = days / 30;
            return months + " months ago";
        } else {
            long years = days / 365;
            return years + " years ago";
        }
    }
}
