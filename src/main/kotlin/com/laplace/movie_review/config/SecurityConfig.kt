package com.laplace.movie_review.config

import com.laplace.movie_review.filter.JwtAuthenticationFilter
import com.laplace.movie_review.service.CustomOAuth2UserService
import com.laplace.movie_review.provider.JwtTokenProvider
import com.laplace.movie_review.service.AuthService
import jakarta.servlet.http.Cookie
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import util.TokenUnit

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oAuth2UserService: CustomOAuth2UserService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val userDetailsService: UserDetailsService,
) {
    @Bean
    fun authenticationProvider(passwordEncoder: PasswordEncoder): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationSuccessHandler(authService: AuthService, jwtTokenProvider: JwtTokenProvider): AuthenticationSuccessHandler {
        return AuthenticationSuccessHandler { request, response, authentication ->
            val userDetails = authentication.principal as UserDetails
            val roles = authentication.authorities.map { it.authority }
            val (accessToken, _) = jwtTokenProvider.generateToken(userDetails.username, roles, TokenUnit.ACCESS_TOKEN)
            response.addCookie(Cookie(TokenUnit.ACCESS_TOKEN.token, accessToken))
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtTokenProvider: JwtTokenProvider): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/", "/login", "/user").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .loginPage("/login")
                    .userInfoEndpoint { userInfoEndPoint ->
                        userInfoEndPoint.userService(oAuth2UserService)
                    }
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .defaultSuccessUrl("/", true)
                    .successHandler()
            }
            .authenticationProvider(authenticationProvider(passwordEncoder()))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .formLogin { form ->
                form.disable()
            }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
            .csrf { csrfConfigurer ->
                csrfConfigurer.disable()
            }

        return http.build()
    }
}
