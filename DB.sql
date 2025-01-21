-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for goal_line_news
DROP DATABASE IF EXISTS `goal_line_news`;
CREATE DATABASE IF NOT EXISTS `goal_line_news` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `goal_line_news`;

-- Dumping structure for table goal_line_news.comments
DROP TABLE IF EXISTS `comments`;
CREATE TABLE IF NOT EXISTS `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `news_id` int NOT NULL,
  `time` timestamp NOT NULL,
  `text` longtext,
  PRIMARY KEY (`id`),
  KEY `news_id` (`news_id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table goal_line_news.news
DROP TABLE IF EXISTS `news`;
CREATE TABLE IF NOT EXISTS `news` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` timestamp NOT NULL,
  `image` text,
  `title` text,
  `content` longtext,
  `views` int NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table goal_line_news.news_has_authors
DROP TABLE IF EXISTS `news_has_authors`;
CREATE TABLE IF NOT EXISTS `news_has_authors` (
  `news_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`news_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `news_has_authors_ibfk_1` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `news_has_authors_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table goal_line_news.news_has_tags
DROP TABLE IF EXISTS `news_has_tags`;
CREATE TABLE IF NOT EXISTS `news_has_tags` (
  `news_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`news_id`,`tag_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `news_has_tags_ibfk_1` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `news_has_tags_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table goal_line_news.tags
DROP TABLE IF EXISTS `tags`;
CREATE TABLE IF NOT EXISTS `tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table goal_line_news.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` enum('ADMIN','USER','WRITER') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` timestamp NOT NULL,
  `google_id` varchar(255) DEFAULT NULL,
  `is_oauth2` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table goal_line_news.users_like_comments
DROP TABLE IF EXISTS `users_like_comments`;
CREATE TABLE IF NOT EXISTS `users_like_comments` (
  `user_id` int NOT NULL,
  `comment_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`comment_id`),
  KEY `FK_users_like_comments_comments` (`comment_id`),
  CONSTRAINT `FK_users_like_comments_comments` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`),
  CONSTRAINT `FK_users_like_comments_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table goal_line_news.users_save_news
DROP TABLE IF EXISTS `users_save_news`;
CREATE TABLE IF NOT EXISTS `users_save_news` (
  `user_id` int NOT NULL,
  `news_id` int NOT NULL,
  `time` timestamp NOT NULL,
  PRIMARY KEY (`user_id`,`news_id`),
  KEY `news_id` (`news_id`),
  CONSTRAINT `users_save_news_ibfk_1` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `users_save_news_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--seed

INSERT INTO `users` (`name`, `email`, `password`, `role`, `created_at`, `google_id`, `is_oauth2`) VALUES
('John Doe', 'johndoe@example.com', '$2a$10$FzPCV3xJx1vHsDfQXWbWhOsbDaIv8iPJuUtN2y/npFWz.D1fFdGAe', 'USER', NOW(), NULL, 0),
('Jane Smith', 'janesmith@example.com', '$2a$10$Wj3m1S2kK01Roj2H9OGgoOenVHrjlhXTFJXmDItJqaUfx9hOWktJ6', 'ADMIN', NOW(), NULL, 0),
('Michael Johnson', 'michaelj@example.com', '$2a$10$PtFFc5R6adwHsWkeJfM.9uTPAoJ9WiRiqbC9q7gbkHg4WFDc8bFSO', 'WRITER', NOW(), NULL, 0),
('Emily Davis', 'emilyd@example.com', '$2a$10$yxwFkZd2RBoLVhYzdtprX.UqVyJ6tU4UtgIN1H2BGqz1KcplEYYBu', 'USER', NOW(), NULL, 0),
('Chris Brown', 'chrisb@example.com', '$2a$10$Jd/9UL4qPXsDwI9fAF8MOeZhsqCtuFEeNdiWpLKqITzJMbWPCVu3W', 'WRITER', NOW(), NULL, 0),
('Sarah Wilson', 'sarahw@example.com', '$2a$10$ZoJprbn1JhZspVq4BWewEOIakGkaBh.R7vIBYoPZqX7ByJlHkUjDS', 'USER', NOW(), NULL, 0),
('David Martinez', 'davidm@example.com', '$2a$10$TAJ4Vc4NBfFVGYOllvj43OYoKJ6oMaBc9jG1/Kv9hci45P.yNViXO', 'ADMIN', NOW(), NULL, 0),
('Sophia Lee', 'sophial@example.com', '$2a$10$XO1ws8HbVvRQvB1Ote7OROC.CWe7ESiSIFyESDgKPdb8ZstQGdsMi', 'WRITER', NOW(), NULL, 0),
('James Anderson', 'jamesa@example.com', '$2a$10$U/jJu62mLBIXx9xsxsxphOiStnxlvGLk2tf/OcszE9SCEuOYeRcPK', 'USER', NOW(), NULL, 0),
('Linda Harris', 'lindah@example.com', '$2a$10$hSyDePGj6SH6PGxqq35MxeBpSa4UO5U1MuPi1T2RcPf2i9M1nOQyS', 'ADMIN', NOW(), NULL, 0),
('Robert Young', 'roberty@example.com', '$2a$10$pAWbLfY6bKrKo6zyMZ/1JOSfpH7HaGMLkrEQI8LAFfYYGGb3p9MOa', 'WRITER', NOW(), NULL, 0),
('Patricia King', 'patriciak@example.com', '$2a$10$S3GKm0E47YFjHOwRLxWsWumOd4rFbAkaBVPSA5ZxRbd5ZY6FEdTeC', 'USER', NOW(), NULL, 0),
('Matthew Wright', 'mattheww@example.com', '$2a$10$n5QxeP6xxmTlwSfgLsHPw.LzH3X89U85f9CTYoc6cF8a1cQcb/WLW', 'WRITER', NOW(), NULL, 0),
('Barbara Scott', 'barbaras@example.com', '$2a$10$M72EZiwKvJce46P43o9qeeR6hZhCmBqPrnYtZ9ylTo5MdyZTnsJ7K', 'ADMIN', NOW(), NULL, 0),
('Richard Walker', 'richardw@example.com', '$2a$10$KoO3MwGn2SRbGeTbWlEwU.BAGd/ERHrsW20cv5e2Ub8KK0wL0MiVm', 'USER', NOW(), NULL, 0);

INSERT INTO tags (id, name) VALUES
(1, 'Sports'),
(2, 'Technology'),
(3, 'Health'),
(4, 'Education'),
(5, 'Travel'),
(6, 'Food'),
(7, 'Entertainment'),
(8, 'Finance'),
(9, 'Business'),
(10, 'Science'),
(11, 'Environment'),
(12, 'Fashion'),
(13, 'Politics'),
(14, 'History'),
(15, 'Music'),
(16, 'Movies'),
(17, 'Gaming'),
(18, 'Books'),
(19, 'Art'),
(20, 'Culture'),
(21, 'Lifestyle'),
(22, 'Parenting'),
(23, 'Fitness'),
(24, 'Wellness'),
(25, 'Pets'),
(26, 'DIY'),
(27, 'Real Estate'),
(28, 'Automobiles'),
(29, 'Photography'),
(30, 'Startups'),
(31, 'Marketing'),
(32, 'Investments'),
(33, 'Cryptocurrency'),
(34, 'Artificial Intelligence'),
(35, 'Machine Learning'),
(36, 'Data Science'),
(37, 'Programming'),
(38, 'Web Development'),
(39, 'Mobile Development'),
(40, 'Productivity'),
(41, 'Mental Health'),
(42, 'Relationships'),
(43, 'Spirituality'),
(44, 'Philosophy'),
(45, 'Astronomy'),
(46, 'Physics'),
(47, 'Chemistry'),
(48, 'Biology'),
(49, 'Mathematics'),
(50, 'Economics');




-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
