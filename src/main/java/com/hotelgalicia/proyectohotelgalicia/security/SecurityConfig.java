package com.hotelgalicia.proyectohotelgalicia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        // PARTE API REST
        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public AuthTokenFilter authenticationJwtTokenFilter() {
                return new AuthTokenFilter();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

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
                                                .requestMatchers("/**", "/Css/**", "/Media/**", "/error","/api-docs")
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
                                .csrf(csrf -> csrf.ignoringRequestMatchers(
                                                AntPathRequestMatcher.antMatcher("/h2-console/**")))
                                .httpBasic(Customizer.withDefaults());
                http.exceptionHandling(exceptions -> exceptions.accessDeniedPage("/accessError"));
                return http.build();
        }

}