package com.laplace.movie_review.config

import com.laplace.movie_review.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class OAuth2SecurityConfig(private val oAuth2UserService: CustomOAuth2UserService) {

    @Bean
    fun oAuth2securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/", "/login", "/h2-console").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .loginPage("/login")
                    .userInfoEndpoint { userInfoEndPoint ->
                        userInfoEndPoint.userService(oAuth2UserService)
                    }
            }
//            .logout { logout ->
//                logout.logoutSuccessUrl("/")
//            }
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
