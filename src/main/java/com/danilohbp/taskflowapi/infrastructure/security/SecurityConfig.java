package com.danilohbp.taskflowapi.infrastructure.security;

import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * API: sem redirect pra /login. Postman usa Basic Auth.
     */
    @Bean
    @Order(1)
    SecurityFilterChain apiSecurity(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable()) // API stateless: sem CSRF
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService) // ✅ FIX: garante provider na chain da API
                .authorizeHttpRequests(auth -> auth
                        // exemplo: permitir health (se tiver)
                        .requestMatchers("/api/health").permitAll()

                        // se quiser: permitir criar usuário sem auth (somente DEV)
                        // .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // ✅ Postman vai usar Basic Auth
                .build();
    }

    /**
     * Views: session + form login.
     */
    @Bean
    @Order(2)
    SecurityFilterChain viewSecurity(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        return http
                .userDetailsService(userDetailsService) // ✅ FIX: garante provider na chain das views
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/tasks/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .build();
    }

    /**
     * ✅ Autenticação via banco (email como username).
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository repo) {
        return username -> repo.findByEmailIgnoreCase(username)
                .map(u -> org.springframework.security.core.userdetails.User
                        .withUsername(u.getEmail())
                        .password(u.getPasswordHash())
                        .roles(u.getRole().name())
                        .disabled(!u.isActive())
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
