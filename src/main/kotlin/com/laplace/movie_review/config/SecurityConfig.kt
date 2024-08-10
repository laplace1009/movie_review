package com.laplace.movie_review.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it
//                    .anyRequest().permitAll()
                    .requestMatchers("/", "/login").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login {
                it.loginPage("/login")
            }
            .formLogin { form -> form.disable() }
            .csrf { csrfConfigurer ->
                csrfConfigurer.disable()
            }

        return http.build()
    }
}