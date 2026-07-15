package com.example.muzayede.config;

import com.example.muzayede.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthanticationFilter extends OncePerRequestFilter {

    public final JwtService jwtService;

    public JwtAuthanticationFilter(JwtService jwtService)
    {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        String username;

        // headerin dolu olmasi gerekiyor
        // ayriyeten Bearer ile baslamasi..
        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer dan sonrasi
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // kullanici adi mevcut ama sistemde henuz auth yapmamissa
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            if (jwtService.isTokenValid(jwt, username))
            {
                String roleName = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(authority)
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
