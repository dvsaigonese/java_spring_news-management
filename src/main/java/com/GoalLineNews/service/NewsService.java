package com.GoalLineNews.service;

import com.GoalLineNews.controller.Backend.NewsController;
import com.GoalLineNews.dto.NewsDTO;
import com.GoalLineNews.dto.TagDTO;
import com.GoalLineNews.entity.News;
import com.GoalLineNews.entity.Tag;
import com.GoalLineNews.entity.User;
import com.GoalLineNews.repository.NewsRepository;
import com.GoalLineNews.repository.TagRepository;
import com.GoalLineNews.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    public NewsDTO createNews(NewsDTO newsDTO, List<Integer> authors) {
        News news = convertDTOToEntity(newsDTO);
        News savedNews = newsRepository.save(news);
        for (int author : authors) {
            addUserToNews(news.getId(), author);
        }
        return convertEntityToDTO(savedNews);
    }

    @Transactional
    public NewsDTO updateNews(int id, NewsDTO newsDTO, List<Integer> authors, List<Integer> tags) {
        Optional<News> newsOptional = newsRepository.findById(id);

        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            news.setTitle(newsDTO.getTitle());
            news.setContent(newsDTO.getContent());
            if (newsDTO.getImage() != null) {
                news.setImage(newsDTO.getImage());
            }
            news.setViews(newsDTO.getViews());
            News updatedNews = newsRepository.save(news);

            updateUserToNews(id, authors);
            updateTagsForNews(id, tags);

            return convertEntityToDTO(updatedNews);
        }
        return newsDTO;
    }

    public NewsDTO updateViewNews(int id, NewsDTO newsDTO) {
        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            news.setViews(news.getViews() + 1);
            News updatedNews = newsRepository.save(news);
            return convertEntityToDTO(updatedNews);
        }
        return newsDTO;
    }

    public void deleteNews(int id) {
        tagRepository.deleteByNewsId(id);
        userRepository.deleteByNewsId(id);
        newsRepository.deleteById(id);
    }

    public List<NewsDTO> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        return newsList.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public NewsDTO getNewsById(int id) {
        Optional<News> newsOptional = newsRepository.findById(id);
        return newsOptional.map(this::convertEntityToDTO).orElse(null);
    }

    public List<NewsDTO> getTop4NewsInHomePage() {
        List<News> newsHomePage = newsRepository.findTop5ByStatusOrderByTimeDesc(1);
        newsHomePage = newsHomePage.size() > 1 ? newsHomePage.subList(1, newsHomePage.size()) : Collections.emptyList();
        return newsHomePage.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public NewsDTO getLatestNews() {
        Optional<News> news = newsRepository.findTopByStatusOrderByTimeDesc(1);
        return news.map(this::convertEntityToDTO).orElse(null);
    }

    public List<NewsDTO> searchNewsByKeyword(String keyword) {
        List<News> searchNews = newsRepository.findTop20ByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByTimeDesc(keyword, keyword);
        return searchNews.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public void addUserToNews(int newsId, int userId) {
        News news = newsRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        news.getAuthors().add(user);
        user.getNews().add(news);

        newsRepository.save(news);
    }

    @Transactional
    public void updateUserToNews(int newsId, List<Integer> authors) {
        userRepository.deleteByNewsId(newsId);

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        for (int authorId : authors) {
            User user = userRepository.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!news.getAuthors().contains(user)) {
                news.getAuthors().add(user);
                user.getNews().add(news);
            }
        }

        newsRepository.save(news);
    }

    @Transactional
    public void updateTagsForNews(int newsId, List<Integer> tagIds) {
        tagRepository.deleteByNewsId(newsId);

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        for (int tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
            if (!news.getTags().contains(tag)) {
                news.getTags().add(tag);
                tag.getNews().add(news);
            }
        }

        newsRepository.save(news);
    }

    private NewsDTO convertEntityToDTO(News news) {
        return modelMapper.map(news, NewsDTO.class);
    }

    private News convertDTOToEntity(NewsDTO newsDTO) {
        return modelMapper.map(newsDTO, News.class);
    }
}
