package com.vivekdev.TaskApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// This is the Entry point of the Application
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //extract the token from the Request
        String token=getTokenFromRequest(request);

        if(token!=null){
            String username=jwtUtils.getUsernameFromToken(token);
         UserDetails userDetails= customUserDetailsService.loadUserByUsername(username);

         if(jwtUtils.isTokenValid(token,userDetails)){
             UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                     userDetails,null,userDetails.getAuthorities()
             );
             authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
             SecurityContextHolder.getContext().setAuthentication(authenticationToken);  // This is going to set the Authenticated User Object in the Global Context; So the Spring can manage the User

         }
        }

        try {
            filterChain.doFilter(request,response);
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }

    private String getTokenFromRequest(HttpServletRequest request){
        String tokenWithBearer=request.getHeader("Authorization");
        if(tokenWithBearer!=null && tokenWithBearer.startsWith("Bearer ")){
            return tokenWithBearer.substring(7);
        }
        return null;
    }
}
