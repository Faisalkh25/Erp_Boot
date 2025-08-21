package com.erp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.erp.filter.JwtFilter;
import com.erp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtFilter jwtFilter;

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                return http.csrf(csrf -> csrf.disable())
                                // .formLogin(form -> form.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/",
                                                                "/api/auth/login",
                                                                "/api/auth/logout",
                                                                "/show-*",
                                                                "/dashboard/**",
                                                                "/css/**", "/js/**", "/images/**", "/assets/**",
                                                                "/uploads/**", "/custom_images/**",
                                                                "/api/employees",
                                                                "/*.html",
                                                                "/dashboard/app/**",
                                                                // "/employee/dashboard/task",
                                                                // "/employee/dashboard/addTask",
                                                                "/employee/dashboard/**",
                                                                "/showHoliday",
                                                                "/showHolidayList",
                                                                "/viewProfile",
                                                                "/landingPage")

                                                .permitAll()
                                                .requestMatchers("/admin/**").hasAuthority("Admin")
                                                .requestMatchers("/api/shifts/**").hasAnyAuthority("Admin", "HR")
                                                // .requestMatchers("/showHoliday").hasAuthority("HR")
                                                // .requestMatchers("/index/**").hasAuthority("HR")
                                                // .requestMatchers("/employee/dashboard/**").hasAuthority("Employee")
                                                .requestMatchers("/api/employee-dashboard/**").hasAuthority("Employee")
                                                .requestMatchers("/api/addTask/**").hasAuthority("Employee")
                                                .requestMatchers("/api/tasks/by-employee-today")
                                                .hasAuthority("Employee")
                                                .requestMatchers("/api/quotes").hasAuthority("Employee")
                                                // .requestMatchers("/api/employees/**").hasAnyAuthority("Admin", "HR",
                                                // "Employee")
                                                .requestMatchers(HttpMethod.GET, "/api/employees/**")
                                                .hasAnyAuthority("Admin", "HR", "Employee")
                                                .requestMatchers(HttpMethod.POST, "/api/employees/**")
                                                .hasAnyAuthority("Admin", "HR")
                                                .requestMatchers(HttpMethod.PUT, "/api/employees/**")
                                                .hasAnyAuthority("Admin", "HR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/employees/**")
                                                .hasAnyAuthority("Admin", "HR", "Employee")

                                                .requestMatchers("/api/salary/**").hasAnyAuthority("Admin", "HR")

                                                .requestMatchers("/api/employees/personal-info/**")
                                                .hasAuthority("Employee")
                                                .requestMatchers("/api/system/**").hasAnyAuthority("HR", "Employee")

                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {

                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

}
