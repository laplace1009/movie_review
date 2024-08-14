package com.laplace.movie_review.filter

import com.laplace.movie_review.provider.JwtTokenProvider
import com.laplace.movie_review.service.TokenService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import util.TokenUnit

@Component
class JwtAuthenticationFilter(
    private val tokenService: TokenService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getJwtFromRequest(request)
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.name ?: throw BadCredentialsException("Do not exist username")
        try {
            if (token != null && tokenService.validateToken(token)) {
                // todo!
            } else {
                val storedRefreshToken = tokenService.getRefreshTokenByEmail(username)
                try {
                    if (storedRefreshToken != null && !tokenService.validateToken(storedRefreshToken.token)) {
                        response.addCookie(Cookie(TokenUnit.REFRESH_TOKEN.token, storedRefreshToken.token))
                        val authorities = tokenService.getRolesFromToken(storedRefreshToken.token)
                        val (accessToken, _) = tokenService.generateToken(username, authorities, TokenUnit.ACCESS_TOKEN)
                        response.addCookie(Cookie(TokenUnit.ACCESS_TOKEN.token, accessToken))
                    } else {
                        val (newRefreshToken, expiresAt) =
                            tokenService.generateToken(username, listOf("ADMIN"), TokenUnit.REFRESH_TOKEN)
                        tokenService.saveToken(username, newRefreshToken, expiresAt)
                        response.addCookie(Cookie(TokenUnit.REFRESH_TOKEN.token, newRefreshToken))
                        val (accessToken, _) = tokenService.generateToken(username, listOf("ADMIN"), TokenUnit.ACCESS_TOKEN)
                        response.addCookie(Cookie(TokenUnit.ACCESS_TOKEN.token, accessToken))
                    }
                } catch (ex: JwtException) {
                    when (ex) {
                        is ExpiredJwtException -> {
                            // todo!
                        }
                    }
                }

            }
        } catch (ex: JwtException) {
            when (ex) {
                is ExpiredJwtException -> {

                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val cookies = request.cookies ?: return null
        return cookies.find { it.name == TokenUnit.ACCESS_TOKEN.token }?.value
    }

    private fun tokenRefresh(ex: JwtException, token: String): String? {
        when (ex) {
            is ExpiredJwtException -> {
                val username = tokenService.getUsernameFromToken(token)
                val roles = tokenService.getRolesFromToken(token)
                val refreshToken = tokenService.generateToken(username, roles, TokenUnit.ACCESS_TOKEN)
                return null
            }
            else -> throw ex
        }
    }
}