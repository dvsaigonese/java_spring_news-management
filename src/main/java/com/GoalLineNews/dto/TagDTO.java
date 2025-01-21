package com.GoalLineNews.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private int id;
    private String name;
    private List<NewsDTO> news;

    @Override
    public String toString() {
        StringBuilder newsTitles = new StringBuilder();
        if (news != null && !news.isEmpty()) {
            for (NewsDTO article : news) {
                newsTitles.append(article.getTitle()).append(", ");
            }
            if (newsTitles.length() > 0) {
                newsTitles.setLength(newsTitles.length() - 2);
            }
        } else {
            newsTitles.append("No news articles");
        }

        return "TagDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", newsTitles=[" + newsTitles.toString() + "]" +
                '}';
    }
}
