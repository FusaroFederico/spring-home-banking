package com.banking.home.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.banking.home.spring.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final UserDetailsService userDetailsService;
	
	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter,
								UserDetailsService userDetailsService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
                  .requestMatchers(
                      "/", "/index.html",
                      "/login.html", "/register.html", 
                      "/dashboard.html", "/transaction.html",
                      "/images/**", "/favicon.ico",
                      "/js/**", "/css/**",
                      "/api/auth/**"
                  ).permitAll()
                  .requestMatchers("/api/users/**").hasAuthority("USER")
                  .anyRequest().authenticated()
              )
          .sessionManagement(session -> session
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )
              .authenticationProvider(authenticationProvider())
              .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
              .build();

    }
    
	@Bean
    DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    	
    	authProvider.setPasswordEncoder(passwordEncoder());
    	
    	return authProvider;
    }
}
