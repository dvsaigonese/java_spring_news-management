package com.GoalLineNews.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String image;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private int views;
    private int status;

    public List<User> getAuthors() {
        if (authors == null) {
            authors = new ArrayList<>();
        }
        return authors;
    }
    public List<Tag> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }



    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime time;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "news")
    private List<User> authors;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "news")
    private List<Tag> tags;
}
