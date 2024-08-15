package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.RefreshToken
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
@DataJpaTest
class RefreshTokenRepositoryTest {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @BeforeEach
    fun beforeEach() {
        accountRepository.deleteAll()
        refreshTokenRepository.deleteAll()
        val account = Account("test", "test@test.com", "1234")
        val storedAccount = accountRepository.save(account)
        refreshTokenRepository.save(RefreshToken(storedAccount, "1234567", LocalDateTime.now()))
    }

    @Test
    fun `test findByEmail return when account exists`() {
        val email = "test@test.com"
        val refreshToken = refreshTokenRepository.findByEmail(email)
        assertNotNull(refreshToken)
        assertEquals("test@test.com", refreshToken?.account?.email)
    }

    @Test
    fun `test save`() {
        val account = Account("test2", "test2@test.com", "1234")
        val storedAccount = accountRepository.save(account)
        val refreshToken = RefreshToken(storedAccount, "1234567", LocalDateTime.now())
        val storedRefreshToken = refreshTokenRepository.save(refreshToken)
        assertNotNull(storedRefreshToken)
        assertEquals(storedRefreshToken, refreshToken)
    }

}