package com.example.demo.security;

import com.example.demo.services.JwtGeneratorService;
import com.example.demo.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtGeneratorService jwtGenerator;
    private final UserService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtGeneratorService jwtGenerator, UserService userService) {
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        var token = request.getHeader("authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var requestURIelements = request.getRequestURI().split("/");
        if (requestURIelements.length<=2 || !Objects.equals(requestURIelements[1], "user")){
            filterChain.doFilter(request, response);
            return;
        }
        var emailInRequest = requestURIelements[2];

        token = token.substring(7);
        var tokenInfo = jwtGenerator.isValidToken(token, emailInRequest);
        if (tokenInfo.IsValid()) {
            var userDetails = userService.loadUserByUsername(tokenInfo.username());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}