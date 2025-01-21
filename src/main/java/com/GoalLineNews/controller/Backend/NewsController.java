package com.GoalLineNews.controller.Backend;

import com.GoalLineNews.GoalLineNewsManagementApplication;
import com.GoalLineNews.dto.CommentDTO;
import com.GoalLineNews.dto.NewsDTO;
import com.GoalLineNews.dto.TagDTO;
import com.GoalLineNews.dto.UserDTO;
import com.GoalLineNews.entity.News;
import com.GoalLineNews.entity.Comment;
import com.GoalLineNews.service.CommentService;
import com.GoalLineNews.service.NewsService;
import com.GoalLineNews.service.TagService;
import com.GoalLineNews.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/create")
    public String create(Model model) {
        List<UserDTO> writers = userService.getUsersByRole(GoalLineNewsManagementApplication.Role.WRITER);
        List<UserDTO> admins = userService.getUsersByRole(GoalLineNewsManagementApplication.Role.ADMIN);

        List<UserDTO> authors = new ArrayList<>();
        authors.addAll(writers);
        authors.addAll(admins);

        List<TagDTO> TagsList = tagService.getAllTags();

        model.addAttribute("tagsList", TagsList);
        model.addAttribute("news", new News());
        model.addAttribute("authors", authors);
        model.addAttribute("csrfToken", "CSRF-Token");
        return "news/create";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        NewsDTO news = newsService.getNewsById(id);
        newsService.updateViewNews(id, news);

        LocalDateTime now = LocalDateTime.now();
        Duration lastNewsDuration = Duration.between(news.getTime(), now);
        news.setDisplayTime(formatTimeElapsed(lastNewsDuration));

        CommentDTO comment = new CommentDTO();
        comment.setNews_id(id);

        model.addAttribute("news", news);
        model.addAttribute("comment", comment);
        return "news/news";
    }

    @GetMapping("/comments/{id}")
    @ResponseBody
    public List<Object[]> getAllComments(@PathVariable("id") int news_id) {
        return commentService.findCommentsByNewsIdWithUserName(news_id);
    }

    @PostMapping("/store")
    public String store(HttpServletRequest request, @RequestParam("file") MultipartFile file, @RequestParam("author_id") List<Integer> authorIdList, NewsDTO newsDTO) {
        String referer = request.getHeader("Referer");
        String thumbsPath = saveThumbsImage(file);
        newsDTO.setImage(thumbsPath);
        newsService.createNews(newsDTO, authorIdList);
        return "redirect:" + referer  + "?success";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        NewsDTO newsDTO = newsService.getNewsById(id);
        List<UserDTO> authorsList = userService.getUsersByNewsId(id);
        List<Integer> authorIds = authorsList.stream()
                .map(UserDTO::getId)
                .collect(Collectors.toList());
        
        List<UserDTO> writers = userService.getUsersByRole(GoalLineNewsManagementApplication.Role.WRITER);
        List<UserDTO> admins = userService.getUsersByRole(GoalLineNewsManagementApplication.Role.ADMIN);
        List<UserDTO> authors = new ArrayList<>();
        authors.addAll(writers);
        authors.addAll(admins);

        List<TagDTO> newsTags = tagService.getTagsByNewsId(id);
        List<Integer> newsTagsIds = newsTags.stream()
                .map(TagDTO::getId)
                .collect(Collectors.toList());

        List<TagDTO> TagsList = tagService.getAllTags();

        model.addAttribute("tagsList", TagsList);
        model.addAttribute("newsTagsIds", newsTagsIds);
        model.addAttribute("news", newsDTO);
        model.addAttribute("authors", authors);
        model.addAttribute("authorIds", authorIds);
        model.addAttribute("csrfToken", "CSRF-Token");
        return "news/edit";
    }

    @PostMapping("/{id}")
    public String update(HttpServletRequest request, @PathVariable("id") int id,  @RequestParam("author_id") List<Integer> authorIdList, @RequestParam("tag_id") List<Integer> tagsList, @RequestParam("file") MultipartFile file, NewsDTO newsDTO) {
        String referer = request.getHeader("Referer");
        NewsDTO newsDTOImage = newsService.getNewsById(id);
        String filePath = "src/main/upload/images/" + newsDTOImage.getImage();
        File oldFile = new File(filePath);

        if (file.isEmpty()) {
            newsService.updateNews(id, newsDTO, authorIdList, tagsList);
        } else if (oldFile.delete() || !file.isEmpty()) {
            String thumbsPath = saveThumbsImage(file);
            newsDTO.setImage(thumbsPath);
            newsService.updateNews(id, newsDTO, authorIdList, tagsList);
            return "redirect:/";
        } else {
            return "Failed to delete file";
        }
        return "redirect:" + referer  + "?success";
    }

    @GetMapping("/delete/{id}")
    public String destroy(HttpServletRequest request, @PathVariable("id") int id) {
        String referer = request.getHeader("Referer");
        try {
            newsService.deleteNews(id);
        } catch (Exception e) {
            // Log the error if needed
            e.printStackTrace();
            // Redirect to an error page or return an error message
            return "redirect:" + referer + "?error";
        }
        newsService.deleteNews(id);
        return "redirect:" + referer  + "?success";
    }

    private String saveThumbsImage(MultipartFile file) {
        try {
            String uploadDir = "src/main/upload/images/";
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String randomFileName = UUID.randomUUID().toString() + fileExtension;

            Path path = Paths.get(uploadDir + randomFileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return randomFileName;
        } catch (IOException e) {
            return "Save thumbs failed";
        }
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
