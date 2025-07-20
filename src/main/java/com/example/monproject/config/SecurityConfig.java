package com.example.monproject.config;

import com.example.monproject.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/register",
                                "/css/**", "/js/**", "/img/**",
                                "/actuality", "/event", "/info",
                                "/faq"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            String roles = authentication.getAuthorities().toString();
                            if (roles.contains("ROLE_ADMIN")) {
                                response.sendRedirect("/admin");
                            } else if (roles.contains("ROLE_DONNEUR")) {
                                response.sendRedirect("/donneur");
                            } else if (roles.contains("ROLE_CENTRE")) {
                                response.sendRedirect("/centre");
                            } else {
                                response.sendRedirect("/login?error=true");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .userDetailsService(userService)
                .build();
    }


}

