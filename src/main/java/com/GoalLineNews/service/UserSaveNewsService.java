package com.GoalLineNews.service;

import com.GoalLineNews.dto.UserSaveNewsDTO;
import com.GoalLineNews.entity.UserSaveNews;
import com.GoalLineNews.entity.compositeKey.UserSaveNewsKey;
import com.GoalLineNews.repository.UserSaveNewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserSaveNewsService {

    @Autowired
    private UserSaveNewsRepository userSaveNewsRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserSaveNewsDTO> getAllUserSavedNews() {
        List<UserSaveNews> savedNews = userSaveNewsRepository.findAll();
        return savedNews.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public UserSaveNewsDTO getUserSavedNewsById(UserSaveNewsKey id) {
        Optional<UserSaveNews> savedNewsOptional = userSaveNewsRepository.findById(id);
        return savedNewsOptional.map(this::convertEntityToDTO).orElse(null);
    }

    public UserSaveNewsDTO createUserSavedNews(UserSaveNewsDTO userSaveNewsDTO) {
        UserSaveNews userSaveNews = convertDTOToEntity(userSaveNewsDTO);
        UserSaveNews saved = userSaveNewsRepository.save(userSaveNews);
        return convertEntityToDTO(saved);
    }

    public void deleteUserSavedNews(UserSaveNewsKey id) {
        userSaveNewsRepository.deleteById(id);
    }

    private UserSaveNewsDTO convertEntityToDTO(UserSaveNews userSaveNews) {
        return modelMapper.map(userSaveNews, UserSaveNewsDTO.class);
    }

    private UserSaveNews convertDTOToEntity(UserSaveNewsDTO userSaveNewsDTO) {
        return modelMapper.map(userSaveNewsDTO, UserSaveNews.class);
    }
}
