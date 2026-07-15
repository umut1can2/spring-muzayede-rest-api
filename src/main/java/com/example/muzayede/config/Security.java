package com.example.muzayede.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Security {

    final public JwtAuthanticationFilter authanticationFilter;

    public Security(JwtAuthanticationFilter authanticationFilter)
    {
        this.authanticationFilter = authanticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthanticationFilter jwtAuthanticationFilter) throws Exception
    {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/users/add-balance/**").hasRole("ADMIN")
                                .requestMatchers("/api/auctions/delete/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/auctions/list").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/bids/**").hasRole("USER")
                                .anyRequest().authenticated()



                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        jwtAuthanticationFilter, UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
