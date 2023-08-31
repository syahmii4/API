package org.finalecorp.scorelabs;

import org.finalecorp.scorelabs.repositories.RolesRepository;
import org.finalecorp.scorelabs.repositories.UsersRepository;
import org.finalecorp.scorelabs.services.AuthUserDetailsService;
import org.finalecorp.scorelabs.utilities.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(AuthUserDetailsService customUserDetailsService, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.authUserDetailsService = customUserDetailsService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;

    }


    @Autowired
    private AuthUserDetailsService authUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(authUserDetailsService).passwordEncoder(encoder());
        return authenticationManagerBuilder.build();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic((httpSecurityHttpBasicConfigurer -> {
            try {
                httpSecurityHttpBasicConfigurer.disable().formLogin((AbstractHttpConfigurer::disable));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })).csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers("/rest/auth/**").permitAll().anyRequest().authenticated())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}

