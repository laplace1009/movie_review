package com.laplace.movie_review.service

import com.laplace.movie_review.dto.token.RefreshTokenSaveDTO
import com.laplace.movie_review.dto.token.toEntity
import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.RefreshToken
import com.laplace.movie_review.provider.JwtTokenProvider
import com.laplace.movie_review.repository.AccountRepository
import com.laplace.movie_review.repository.RefreshTokenRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import javax.security.auth.login.AccountNotFoundException

@SpringBootTest
class TokenServiceTest {
    @Mock
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Mock
    lateinit var accountRepository: AccountRepository

    @Mock
    lateinit var jwtTokenProvider: JwtTokenProvider

    @InjectMocks
    lateinit var tokenService: TokenService

    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var token: String
    lateinit var expiresAt: Date

    @BeforeEach
    fun setUp() {
        username = "test"
        email = "test@test.com"
        password = "1234"
        token = "test_token"
        expiresAt = Date()
    }

    @Test
    fun `saveToken should return token`() {
        val account = Account(username, email, password).apply { id = 1L }
        val refreshSaveTokenDto = RefreshTokenSaveDTO(email, token, expiresAt)
        `when`(accountRepository.findByEmail(email)).thenReturn(account)
        `when`(refreshTokenRepository.save(any(RefreshToken::class.java))).thenReturn(refreshSaveTokenDto.toEntity(account))
        val result = tokenService.saveToken(refreshSaveTokenDto)
        assertEquals(result.token, token)
    }

    @Test
    fun `saveToken should return empty result`() {
        val account = Account(username, email, password).apply { id = 1L }
        val refreshSaveTokenDto = RefreshTokenSaveDTO(email, token, expiresAt)
        `when`(accountRepository.findByEmail(email)).thenReturn(null)
        val expectation = assertThrows(AccountNotFoundException::class.java) {
            tokenService.saveToken(refreshSaveTokenDto)
        }

        assertEquals(expectation.message, "${account.email} not found")
    }
}