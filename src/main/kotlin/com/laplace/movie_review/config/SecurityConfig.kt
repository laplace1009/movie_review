package com.laplace.movie_review.config

import com.laplace.movie_review.filter.JwtAuthenticationFilter
import com.laplace.movie_review.service.CustomOAuth2UserService
import com.laplace.movie_review.provider.JwtTokenProvider
import com.laplace.movie_review.repository.RefreshTokenRepository
import com.laplace.movie_review.service.AuthService
import com.laplace.movie_review.service.TokenService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
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
    private val authService: AuthService,
    private val tokenService: TokenService
) {
    @Bean
    fun authenticationProvider(passwordEncoder: PasswordEncoder): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(authService)
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
    fun authenticationSuccessHandler(
        jwtTokenProvider: JwtTokenProvider,
    ): AuthenticationSuccessHandler {
        return AuthenticationSuccessHandler { request, response, authentication ->
            val userDetails = authentication.principal as UserDetails
            val roles = authentication.authorities.map { it.authority }
            val storedRefreshToken = tokenService.getRefreshTokenByEmail(userDetails.username)
            try {
                if (storedRefreshToken != null && !tokenService.validateToken(storedRefreshToken.token)) {
                    response.addCookie(Cookie(TokenUnit.REFRESH_TOKEN.token, storedRefreshToken.token))
                    val (accessToken, _) = jwtTokenProvider.generateToken(userDetails.username, roles, TokenUnit.ACCESS_TOKEN)
                    response.addCookie(Cookie(TokenUnit.ACCESS_TOKEN.token, accessToken))
                }
            } catch (ex: JwtException) {
                when (ex) {
                    is ExpiredJwtException -> {
                        val (newRefreshToken, expiresAt) = tokenService.generateToken(userDetails.username, roles, TokenUnit.REFRESH_TOKEN)
                        tokenService.saveToken(userDetails.username, newRefreshToken, expiresAt)
                        response.addCookie(Cookie(TokenUnit.REFRESH_TOKEN.token, newRefreshToken))
                        val (accessToken, _) = jwtTokenProvider.generateToken(userDetails.username, roles, TokenUnit.ACCESS_TOKEN)
                        response.addCookie(Cookie(TokenUnit.ACCESS_TOKEN.token, accessToken))
                    }
                }
            }
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtTokenProvider: JwtTokenProvider): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/", "/login","/account").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .successHandler(authenticationSuccessHandler(jwtTokenProvider))
                    .defaultSuccessUrl("/", true)
                    .permitAll()
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .loginPage("/login")
                    .userInfoEndpoint { userInfoEndPoint ->
                        userInfoEndPoint.userService(oAuth2UserService)
                    }
            }
            .authenticationProvider(authenticationProvider(passwordEncoder()))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
            .csrf { csrfConfigurer ->
                csrfConfigurer.disable()
            }

        return http.build()
    }
}
