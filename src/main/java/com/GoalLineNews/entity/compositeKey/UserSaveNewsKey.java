package com.GoalLineNews.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveNewsKey implements Serializable {
    @Column(name = "user_id")
    int user_id;
    @Column(name = "news_id")
    int news_id;
}
