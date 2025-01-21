package com.GoalLineNews.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NewsDTO {
    private int id;
    private String title;
    private String image;
    private String content;
    private int views;
    private int status;
    private LocalDateTime time;
    private List<UserDTO> authors;
    private List<TagDTO> tags;
    private String displayTime;

    @Override
    public String toString() {
        StringBuilder authorsNames = new StringBuilder();
        if (authors != null && !authors.isEmpty()) {
            for (UserDTO author : authors) {
                authorsNames.append(author.getName()).append(", ");
            }
            if (authorsNames.length() > 0) {
                authorsNames.setLength(authorsNames.length() - 2);
            }
        } else {
            authorsNames.append("No authors");
        }

        StringBuilder tagNames = new StringBuilder();
        if (tags != null && !tags.isEmpty()) {
            for (TagDTO tag : tags) {
                tagNames.append(tag.getName()).append(", ");
            }
            if (tagNames.length() > 0) {
                tagNames.setLength(tagNames.length() - 2);
            }
        } else {
            tagNames.append("No tags");
        }

        return "NewsDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", views=" + views +
                ", time=" + time +
                ", authors=[" + authorsNames.toString() + "]" +
                ", tags=[" + tagNames.toString() + "]" +
                '}';
    }
}
