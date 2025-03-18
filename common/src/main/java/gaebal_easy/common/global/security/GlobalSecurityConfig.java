package gaebal_easy.common.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class GlobalSecurityConfig {

    GlobalSecurityContextFilter globalSecurityContextFilter;

    public SecurityFilterChain globalSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())

                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(globalSecurityContextFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
