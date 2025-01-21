package com.GoalLineNews.controller.Backend;

import com.GoalLineNews.dto.TagDTO;
import com.GoalLineNews.entity.Tag;
import com.GoalLineNews.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/tags")
public class AdminTagController {

    @Autowired
    private TagService tagService;


    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag/create";
    }

    // Get a tag by ID
    @GetMapping("/edit/{id}")
    public String getTagById(@PathVariable("id") int id, Model model) {
        TagDTO tagDTO = tagService.getTagById(id);
        model.addAttribute("tag", tagDTO);
        return "admin/tag/edit";
    }

    // Create a new tag
    @PostMapping("/store")
    public String store(HttpServletRequest request, TagDTO tagDTO) {
        String referer = request.getHeader("Referer");
        try {
            tagService.createTag(tagDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer + "?error";
        }
        return "redirect:" + referer  + "?success";
    }

    // Update an existing tag
    @PostMapping("/{id}")
    public String updateTag(HttpServletRequest request, @PathVariable int id, TagDTO tagDTO) {
        String referer = request.getHeader("Referer");
        try {
            tagService.updateTag(id, tagDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer + "?error";
        }
        return "redirect:" + referer  + "?success";
    }

    @GetMapping("/delete/{id}")
    public String destroy(HttpServletRequest request, @PathVariable("id") int id) {
        String referer = request.getHeader("Referer");
        try {
            tagService.deleteTag(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer + "?error";
        }
        return "redirect:" + referer  + "?success";
    }

}
