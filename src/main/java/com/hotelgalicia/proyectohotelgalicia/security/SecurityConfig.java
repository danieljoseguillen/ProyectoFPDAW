package com.hotelgalicia.proyectohotelgalicia.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        // PARTE ORIGINAL
        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.headers(headersConfigurer -> headersConfigurer
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
                http.authorizeHttpRequests(
                                auth -> auth
                                                // .requestMatchers("/h2-console/**").hasRole("ADMIN")
                                                .requestMatchers("/**", "/Css/**", "/Media/**", "/error", "/api-docs")
                                                .permitAll()
                                                .requestMatchers("/usuarios/**").hasAnyRole("ADMIN")
                                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout((logout) -> logout
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
                http.exceptionHandling(exceptions -> exceptions.accessDeniedPage("/accessError"));
                return http.build();
        }

}