package com.GoalLineNews.dto;

import com.GoalLineNews.GoalLineNewsManagementApplication;
import com.GoalLineNews.entity.News;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private int id;
    private int isOAuth2;
    private String name;
    private String email;
    private String password;
    private String googleId;
    @Enumerated(EnumType.STRING)
    private GoalLineNewsManagementApplication.Role role;
    private List<NewsDTO> news;
}
