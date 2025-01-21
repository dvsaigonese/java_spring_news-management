package com.GoalLineNews.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private int id;
    private String text;
    private int user_id;
    private int news_id;
    private LocalDateTime time;
}
