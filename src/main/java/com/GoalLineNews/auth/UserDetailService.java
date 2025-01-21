package com.GoalLineNews.auth;

import com.GoalLineNews.entity.User;
import com.GoalLineNews.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (findUser.getIsOAuth2() == 1) {
            throw new UsernameNotFoundException("This account is registered with OAuth2. Please use Google login.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);


        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UnifiedUserDetails(
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    null,
                    null,
                    user.getId()
            );
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}