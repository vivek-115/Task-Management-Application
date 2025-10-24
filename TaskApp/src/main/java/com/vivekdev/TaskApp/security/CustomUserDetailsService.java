package com.vivekdev.TaskApp.security;

import com.vivekdev.TaskApp.entity.User;
import com.vivekdev.TaskApp.exceptions.NotFoundException;
import com.vivekdev.TaskApp.repo.UserRepositry;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositry userRepositry;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepositry.findByUsername(username)
                .orElseThrow(()->new NotFoundException("User Not Found"));

        return AuthUser.builder()
                .user(user)
                .build();

    }
}
