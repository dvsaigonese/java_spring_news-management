package com.GoalLineNews.entity;

import com.GoalLineNews.entity.compositeKey.UserSaveNewsKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_save_news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveNews {
    @EmbeddedId
    UserSaveNewsKey id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("news_id")
    @JoinColumn(name = "news_id")
    private News news;
}
