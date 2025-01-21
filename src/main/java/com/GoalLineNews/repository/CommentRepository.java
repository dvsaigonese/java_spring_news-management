package com.GoalLineNews.repository;

import com.GoalLineNews.entity.Comment;
import com.GoalLineNews.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c.id, c.text, u.name, c.time FROM Comment c JOIN c.user u WHERE c.news.id = :newsId ORDER BY c.time DESC")
    List<Object[]> findCommentsByNewsIdWithUserNameOrderByCommentsTimeDesc(@Param("newsId") int newsId);
    @Query(value = "SELECT COUNT(*) FROM users_like_comments ulc WHERE ulc.comment_id = :commentId", nativeQuery = true)
    int findCommentsLikeByCommentId(@Param("commentId") int commentId);

    @Query(value = "SELECT * FROM comments c JOIN users_like_comments ulc WHERE ulc.user_id = :userId AND c.id = :commentId", nativeQuery = true)
    Optional<Object> findUserLikeComment(@Param("userId") int userId, @Param("commentId") int commentId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users_like_comments (user_id, comment_id) values (:userId, :commentId)", nativeQuery = true)
    int insertUserLikeComment(@Param("userId") int userId, @Param("commentId") int commentId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users_like_comments WHERE user_id = :userId AND comment_id = :commentId", nativeQuery = true)
    void deleteUserLikeComment(@Param("userId") int userId, @Param("commentId") int commentId);
}
