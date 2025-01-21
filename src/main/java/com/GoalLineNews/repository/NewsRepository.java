package com.GoalLineNews.repository;

import com.GoalLineNews.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findTop5ByStatusOrderByTimeDesc(int status);
    Optional<News> findTopByStatusOrderByTimeDesc(int status);
    List<News> findTop20ByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByTimeDesc(String titleKeyword, String contentKeyword);
}
