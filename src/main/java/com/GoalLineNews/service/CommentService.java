package com.GoalLineNews.service;

import com.GoalLineNews.dto.CommentDTO;
import com.GoalLineNews.entity.Comment;
import com.GoalLineNews.entity.News;
import com.GoalLineNews.entity.User;
import com.GoalLineNews.repository.CommentRepository;
import com.GoalLineNews.repository.NewsRepository;
import com.GoalLineNews.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ModelMapper modelMapper;

    public List<Object[]>  findCommentsByNewsIdWithUserName(int news_id) {
        List<Object[]> comments = commentRepository.findCommentsByNewsIdWithUserNameOrderByCommentsTimeDesc(news_id);
        List<Object[]> formattedComments = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Object[] comment : comments) {
            int id = (int) comment[0];
            String text = (String) comment[1];
            String userName = (String) comment[2];

            int likes = commentRepository.findCommentsLikeByCommentId(id);

            // Tính thời gian đã trôi qua và định dạng
            Duration duration = Duration.between(((LocalDateTime) comment[3]), now);
            String formattedTime = formatTimeElapsed(duration);

            // Thêm comment đã định dạng lại vào danh sách kết quả
            formattedComments.add(new Object[]{id, text, userName, formattedTime, likes});
        }

        return formattedComments;
    }

    public Object[] findLatestCommentByNewsId(int newsId) {
        List<Object[]> comments = findCommentsByNewsIdWithUserName(newsId);

        if (!comments.isEmpty()) {
            return comments.get(0);
        }

        return null;
    }


    public CommentDTO getCommentById(int id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        return commentOptional.map(this::convertEntityToDTO).orElse(null);
    }

    public Object[] createComment(CommentDTO commentDTO) {
        Comment savedComment = convertDTOToEntity(commentDTO);
        commentRepository.save(savedComment);

        int id = (int) savedComment.getId();
        String text = (String) savedComment.getText();
        String userName = (String) savedComment.getUser().getName();

        int likes = commentRepository.findCommentsLikeByCommentId(id);

        LocalDateTime now = LocalDateTime.now();

        // Tính thời gian đã trôi qua và định dạng
        Duration duration = Duration.between(((LocalDateTime) savedComment.getTime()), now);
        String formattedTime = formatTimeElapsed(duration);

        // Thêm comment đã định dạng lại vào danh sách kết quả
        Object[] newComment = new Object[]{id, text, userName, formattedTime, likes};
        return newComment;
    }

    public CommentDTO updateComment(int id, CommentDTO commentDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setText(commentDTO.getText());
            // Update other fields as needed

            Comment updatedComment = commentRepository.save(comment);
            return convertEntityToDTO(updatedComment);
        }
        return null;
    }

    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }

    public int userLikeComment(int userId, int commentId) {
        int userLikeComment = commentRepository.insertUserLikeComment(userId, commentId);
        return userLikeComment;
    }

    public void userUnlikeComment(int userId, int commentId) {
        commentRepository.deleteUserLikeComment(userId, commentId);
    }

    public int findUserLikeComment(int userId, int commentId) {
        int isLiked = 0;
        Optional<Object> userLikeComment = commentRepository.findUserLikeComment(userId, commentId);
        if (userLikeComment.isPresent()) {
            isLiked = 1;
        }
        return isLiked;
    }

    private CommentDTO convertEntityToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    private Comment convertDTOToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();

        comment.setText(commentDTO.getText());

        News news = newsRepository.findById(commentDTO.getNews_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid news ID"));
        User user = userRepository.findById(commentDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid news ID"));

        comment.setNews(news);
        comment.setUser(user);
        return comment;
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
