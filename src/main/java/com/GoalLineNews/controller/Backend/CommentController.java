package com.GoalLineNews.controller.Backend;

import com.GoalLineNews.auth.UnifiedUserDetails;
import com.GoalLineNews.dto.CommentDTO;
import com.GoalLineNews.service.CommentService;
import com.GoalLineNews.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/store")
    public String store(@AuthenticationPrincipal UnifiedUserDetails userDetails, HttpServletRequest request, @ModelAttribute("comment") CommentDTO commentDTO) {
        String referer = request.getHeader("Referer");
        commentDTO.setUser_id(userDetails.getDatabaseId());
        commentService.createComment(commentDTO);
        return "redirect:" + referer;
    }

    @GetMapping("/like/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeComment(@AuthenticationPrincipal UnifiedUserDetails userDetails, @PathVariable("comment-id") int commentId){
        if (userDetails != null) {
            int isLiked = commentService.findUserLikeComment(userDetails.getDatabaseId(), commentId);
            if (isLiked == 0) {
                commentService.userLikeComment(userDetails.getDatabaseId(), commentId);
            } else if (isLiked == 1) {
                commentService.userUnlikeComment(userDetails.getDatabaseId(), commentId);
            }
        }
    }
}
