package com.GoalLineNews.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSaveNewsDTO {
    private int userId;
    private int newsId;
    private LocalDateTime time;
    private UserDTO user;
    private NewsDTO news;

    @Override
    public String toString() {
        return "UserSaveNewsDTO{" +
                "userId=" + userId +
                ", newsId=" + newsId +
                ", time=" + time +
                ", user=" + (user != null ? user.getName() : "null") +  // Hoặc user.toString()
                ", news=" + (news != null ? news.getTitle() : "null") +  // Hoặc news.toString()
                '}';
    }
}
