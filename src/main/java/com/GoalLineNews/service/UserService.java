package com.GoalLineNews.service;

import com.GoalLineNews.GoalLineNewsManagementApplication;
import com.GoalLineNews.dto.UserDTO;
import com.GoalLineNews.entity.User;
import com.GoalLineNews.repository.UserRepository;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return modelMapper.map(user, UserDTO.class);
        } else {
            return null;
        }
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::convertEntityToDTO).orElse(null);
    }
    public List<UserDTO> getUsersByNewsId(int newsId) {
        List<User> users = userRepository.findAuthorsByNewsId(newsId);
        return users.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByRole(GoalLineNewsManagementApplication.Role role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public String createUser(UserDTO userDTO) {
        try {
            User user = convertDTOToEntity(userDTO);
//            user.setRole(GoalLineNewsManagementApplication.Role.ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            convertEntityToDTO(savedUser);
            return "Added User Successfully";
        } catch (Exception e) {
            return  "Unexpected error while adding user:" + e;
        }
    }
    public String addUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "Added User Successfully";
        } catch (Exception e) {
            return  "Unexpected error while adding user:" + e;
        }
    }

//    public String addUser(UserInfo userInfo) {
//        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
//        repository.save(userInfo);
//        return "Thêm user thành công!";
//    }

    public UserDTO updateUser(int id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());

            User updatedUser = userRepository.save(user);
            return convertEntityToDTO(updatedUser);
        }
        return null;
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertEntityToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertDTOToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
