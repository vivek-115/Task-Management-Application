package com.vivekdev.TaskApp.security;

import com.vivekdev.TaskApp.entity.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
/*
    We need to convert User Entity into UserDetails which is actually understood by Spring Security
    so for that we're using UserDetails service and then we are trying to fit in all the neccessary info right there

* */
@Builder
@Data
public class AuthUser implements UserDetails {

    private User user;

    //We are fitting in the Role from the Entity so that the Spring Security can take the Role Details from here
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRoles().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


}

//Next we will be converting the UserDetails into UserDetails Service
