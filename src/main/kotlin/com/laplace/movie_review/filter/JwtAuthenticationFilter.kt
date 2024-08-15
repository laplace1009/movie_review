package com.laplace.movie_review.filter

import com.laplace.movie_review.service.TokenService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import util.Roles
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
        getJwtFromRequest(request, TokenUnit.ACCESS_TOKEN)?.let { cookieAccessToken ->
            try {
                if (tokenService.validateToken(cookieAccessToken)) {
                    val authentication = tokenService.getAuthentication(cookieAccessToken)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (ex: JwtException) {
                handleJwtException(request, response, ex)
            }
        } ?: run {
            val userInfo = SecurityContextHolder.getContext().authentication
            userInfo?.let { user ->
                val refreshTokenEntity = tokenService.getRefreshTokenByEmail(user.name)
                refreshTokenEntity?.let { entity ->
                    try {
                        if (tokenService.validateToken(entity.token)) {
                            val name = user.name
                            val roles = user.authorities.map { it.authority }
                            generateAndAddTokenToResponse(response, name, roles, TokenUnit.ACCESS_TOKEN)
                        }
                    } catch (ex: JwtException) {
                        handleRefreshTokenExpiration(response, ex)
                    }
                } ?: run {
                    val name = user.name
                    val roles = user.authorities.map { it.authority }
                    generateAndAddTokenToResponse(response,name, roles, TokenUnit.REFRESH_TOKEN)
                    generateAndAddTokenToResponse(response,name, roles, TokenUnit.ACCESS_TOKEN)
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun handleJwtException(request: HttpServletRequest, response: HttpServletResponse, ex: JwtException) {
        if (ex is ExpiredJwtException) {
            val expiredJwtEx: ExpiredJwtException = ex as ExpiredJwtException
            getJwtFromRequest(request, TokenUnit.REFRESH_TOKEN)?.let { cookieRefreshToken ->
                try {
                    if (tokenService.validateToken(cookieRefreshToken)) {
                        val username = expiredJwtEx.claims.subject
                        val roles = tokenService.getRolesFromToken(cookieRefreshToken)
                        generateAndAddTokenToResponse(response, username, roles, TokenUnit.ACCESS_TOKEN)
                    }
                } catch (refreshException: JwtException) {
                    handleRefreshTokenExpiration(response, refreshException)
                }
            } ?: handleRefreshTokenExpiration(response, ex)
        }
    }

    private fun handleRefreshTokenExpiration(response: HttpServletResponse, ex: JwtException) {
        if (ex is ExpiredJwtException) {
            val expiredJwtEx = ex as ExpiredJwtException
            val username = expiredJwtEx.claims.subject
            val roles = listOf(Roles.ADMIN.role)
            generateAndAddTokenToResponse(response, username, roles, TokenUnit.REFRESH_TOKEN)
            generateAndAddTokenToResponse(response, username, roles, TokenUnit.ACCESS_TOKEN)
        }
    }

    private fun generateAndAddTokenToResponse(
        response: HttpServletResponse,
        username: String, roles: List<String>,
        tokenUnit: TokenUnit
    ) {
        val (token, expiresAt) = tokenService.generateToken(username, roles, tokenUnit)
        if (tokenUnit == TokenUnit.REFRESH_TOKEN) {
            tokenService.saveToken(username, token, expiresAt)
        }
        response.addCookie(Cookie(tokenUnit.token, token))
    }

    private fun getJwtFromRequest(request: HttpServletRequest, tokenUnit: TokenUnit): String? {
        val cookies = request.cookies ?: return null
        return cookies.find { it.name == tokenUnit.token }?.value
    }
}