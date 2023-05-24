package com.service.expensemanagement.service.token;

import com.service.expensemanagement.helper.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(CustomUserDetailsServiceImpl customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Token not present");
            return;
        }

        String token = authorization.split("Bearer ")[1];
        String username;
        try {
            username = jwtUtil.extractUsername(token);
        }catch (Exception ex){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid Token");
            return;
        }

        if (username != null){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if(!jwtUtil.validateToken(token, userDetails)){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("swagger-ui") || path.contains("v2") ||
                path.contains("token") || path.contains("swagger-resources");
    }
}
