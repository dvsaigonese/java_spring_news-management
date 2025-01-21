package com.GoalLineNews.auth;

import com.GoalLineNews.GoalLineNewsManagementApplication;
import com.GoalLineNews.auth.UnifiedUserDetails;
import com.GoalLineNews.entity.User;
import com.GoalLineNews.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        if (oAuth2User == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "OAuth2User is null", null));
        }

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub"); // ID duy nhất từ Google

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email attribute is missing");
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(oAuth2User.getAttribute("name"));
            newUser.setPassword((UUID.randomUUID().toString())); // OAuth2 không yêu cầu mật khẩu
            newUser.setRole(GoalLineNewsManagementApplication.Role.USER);
            newUser.setIsOAuth2(1);
            newUser.setGoogleId(googleId);
            return userRepository.save(newUser);
        });

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("googleId", googleId);
        attributes.put("name", name); // Thêm tên
        attributes.put("email", email); // Thêm email

        return new UnifiedUserDetails(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                oAuth2User.getAttributes(),
                user.getGoogleId(),
                user.getId()
        );
    }
}
