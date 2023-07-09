package ru.eljke.tournamentsystem.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import ru.eljke.tournamentsystem.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private UserServiceImpl service;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(service).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .requestCache((cache) -> cache
                        .requestCache(new HttpSessionRequestCache()).disable()
                )
                .passwordManagement((management) -> management
                        .changePasswordPage("/change-password"))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests (auth -> auth
                        .requestMatchers("", "/", "/home", "/register", "/login", "/change-password/**",
                                "/static/**", "/images/**", "/styles/**","/scripts/**", "/docs/**",
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/favicon.*",
                                "/teams/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/tournaments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/tournaments/**").hasAnyAuthority("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/tournaments/**").hasAnyAuthority("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/tournaments/**").hasAnyAuthority("ADMIN", "TEACHER")
                        .requestMatchers("/users/**", "/admin/**").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



