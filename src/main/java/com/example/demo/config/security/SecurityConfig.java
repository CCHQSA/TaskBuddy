package com.example.demo.config.security;

import com.example.demo.config.security.jwt.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;
import java.util.Base64;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtFilter jwtFilter;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;


    public SecurityConfig(JwtFilter jwtFilter,
                          PasswordEncoder passwordEncoder,
                          UserDetailsService userDetailsService
    ) {
        this.jwtFilter = jwtFilter;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                "/",
                                "/worksearch",
                                "/register",
                                "/login",
                                "/css/**",
                                "/js//**",
                                "images/**",
                                "/favicon.ico",
                                "swagger-ui.html",
                                "swagger-ui/**",
                                "v3/api-docs/**"
                        ).permitAll()
                                .anyRequest().authenticated()
                ).anonymous(anon
                        -> anon.principal("anonymousUser")
                ).formLogin(login ->
                        login.loginPage("/login")
                                .loginProcessingUrl("/login")
                                .permitAll()
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/home", true)
                                .permitAll()
                ).logout(logout ->
                        logout.logoutUrl("/logout")
                                .deleteCookies("JSESSIONID", "JWT_TOKEN")
                                .logoutSuccessUrl("/login")
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
