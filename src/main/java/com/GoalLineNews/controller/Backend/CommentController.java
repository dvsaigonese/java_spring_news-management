package com.GoalLineNews.controller.Backend;

import com.GoalLineNews.auth.UnifiedUserDetails;
import com.GoalLineNews.dto.CommentDTO;
import com.GoalLineNews.service.CommentService;
import com.GoalLineNews.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @PostMapping("/store")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void store(@AuthenticationPrincipal UnifiedUserDetails userDetails, HttpServletRequest request, @ModelAttribute("comment") CommentDTO commentDTO) {
        String referer = request.getHeader("Referer");
        commentDTO.setUser_id(userDetails.getDatabaseId());
        commentService.createComment(commentDTO);
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

    @GetMapping("/latest/{news-id}")
    @ResponseBody
    public Object[] getLatestComment(@PathVariable("news-id") int newsId) {
        Object[] latestComment = commentService.findLatestCommentByNewsId(newsId);
        return latestComment;
    }

    @MessageMapping("/send") // Tin nhắn từ client gửi tới /app/comment/send
    @SendTo("/topic/socket") // Phản hồi tới các client đang subscribe /comment/socket
    public Object[] handleCommentSocket(@AuthenticationPrincipal UnifiedUserDetails userDetails, @Payload Map<String, Object> payload) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            log.info("User Principal: " + authentication.getPrincipal());
//        }
//        if (userDetails == null) {
//            log.error("UserDetails is null. The user might not be authenticated.");
//            return new Object[]{"Error: User not authenticated"};
//        }
//        if (userDetails.getDatabaseId() == null) {
//            log.error("User ID is null in UserDetails: " + userDetails);
//            return new Object[]{"Error: User ID is null"};
//        }

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText(payload.get("text").toString());
        commentDTO.setNews_id(Integer.parseInt(payload.get("news_id").toString()));
        commentDTO.setUser_id(Integer.parseInt(payload.get("user_id").toString()));

        // Chuyển String time -> LocalDateTime
        commentDTO.setTime(Instant.parse(payload.get("time").toString()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        return commentService.createComment(commentDTO);
    }

//    @MessageMapping("/send")
//    @SendTo("/topic/socket")  // Đẩy dữ liệu đến các client subscribe tại /topic/socket
//    public CompletableFuture<Object[]> handleCommentSocket(@Payload Map<String, Object> payload) {
//        int newsId = Integer.parseInt(payload.get("newsId").toString());
//        // Gửi dữ liệu comment vào DB trước khi lấy lại comment mới nhất
//        return CompletableFuture.supplyAsync(() -> {
//            Object[] latestComment = commentService.findLatestCommentByNewsId(newsId);
//            return latestComment;  // Trả về comment mới nhất
//        });
//    }

}
