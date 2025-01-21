package com.GoalLineNews.repository;

import com.GoalLineNews.entity.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(value = "SELECT t.tag_id FROM news_has_tags t WHERE t.news_id = :newsId", nativeQuery = true)
    List<Integer> findTagIdsByNewsId(@Param("newsId") int newsId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM news_has_tags nt WHERE nt.news_id = :newsId", nativeQuery = true)
    void deleteByNewsId(@Param("newsId") int newsId);
}
