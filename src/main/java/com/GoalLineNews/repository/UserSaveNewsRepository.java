package com.GoalLineNews.repository;


import com.GoalLineNews.entity.UserSaveNews;
import com.GoalLineNews.entity.compositeKey.UserSaveNewsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSaveNewsRepository extends JpaRepository<UserSaveNews, UserSaveNewsKey>  {
}
