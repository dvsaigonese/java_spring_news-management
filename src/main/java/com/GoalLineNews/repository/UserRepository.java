package com.GoalLineNews.repository;

import com.GoalLineNews.GoalLineNewsManagementApplication;
import com.GoalLineNews.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(Integer integer);

    Optional<User> findByEmail(String email);

    List<User> findByRole(GoalLineNewsManagementApplication.Role role);

    @Query("SELECT u FROM User u JOIN u.news n WHERE n.id = :newsId")
    List<User> findAuthorsByNewsId(@Param("newsId") int newsId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM news_has_authors WHERE news_id = :newsId", nativeQuery = true)
    void deleteByNewsId(@Param("newsId") int newsId);
}
