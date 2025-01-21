package com.GoalLineNews.auth;

import com.GoalLineNews.GoalLineNewsManagementApplication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class UnifiedUserDetails implements UserDetails, OAuth2User {
    private final String name;
    private final String password;
    private final String email;
    private final GoalLineNewsManagementApplication.Role role;
    private final Map<String, Object> attributes;


    private final String googleId;
    private final Integer databaseId;

    public UnifiedUserDetails(String name, String email, String password,
                              GoalLineNewsManagementApplication.Role role,
                              Map<String, Object> attributes,
                              String googleId, Integer databaseId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.attributes = attributes;
        this.googleId = googleId;
        this.databaseId = databaseId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public GoalLineNewsManagementApplication.Role getRole() {
        return role;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getGoogleId() {
        return googleId;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }
}

