package com.pdp.ecommerce.security;

import com.pdp.ecommerce.entity.Role;
import com.pdp.ecommerce.exception.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        try{
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);
                if (jwtUtils.isValid(token)) {
                    String email = jwtUtils.getEmail(token);
                    List<Role> roles = jwtUtils.getRoles(token);
                    var auth =  new UsernamePasswordAuthenticationToken(email, null, roles);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response);
        }catch (TokenExpiredException e){
            handleException(response);
        }
    }

    private void handleException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{ \"error\": \"token is expired\" }");
    }
}
