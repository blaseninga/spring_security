package com.zaurtregulov.spring.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class MySecurityConfig  {
    @Autowired
    DataSource dataSource;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").hasAnyRole("EMPLOYEE", "HR", "MANAGER")
                        .requestMatchers("/hr_info").hasAnyRole("HR")
                        .requestMatchers("/manager_info").hasAnyRole("MANAGER")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        return users;
    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(
////                User.builder()
////                        .username("zaur")
////                        .password(passwordEncoder().encode("zaur"))
////                        .roles("EMPLOYEE")
////                        .build(),
////                User.builder()
////                        .username("elena")
////                        .password(passwordEncoder().encode("elena"))
////                        .roles("HR")
////                        .build(),
////                User.builder()
////                        .username("ivan")
////                        .password(passwordEncoder().encode("ivan"))
////                        .roles("MANAGER", "HR")
////                        .build()
//        );
//    }
@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
}
