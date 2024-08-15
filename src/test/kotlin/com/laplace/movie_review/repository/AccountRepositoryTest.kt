package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Account
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @BeforeEach
    fun beforeEach() {
        accountRepository.deleteAll()

        val account = Account("test", "test@test.com", "1234")

        accountRepository.save(account)
    }

    @Test
    fun `test findByEmail returns when email exists`() {
        val email = "test@test.com"

        val foundAccount = accountRepository.findByEmail(email)

        assertNotNull(foundAccount)
        assertEquals("test", foundAccount?.username)
        assertEquals("test@test.com", foundAccount?.email)
        assertEquals("1234", foundAccount?.password)

    }

    @Test
    fun `test findByEmail returns when email does not exist`() {
        val email = "notExist@test.com"

        val foundAccount = accountRepository.findByEmail(email)

        assertNull(foundAccount)
    }

    @Test
    fun `test save and findById`() {
        val newAccount = Account("new", "new@test.com", "1234")
        val savedAccount = accountRepository.save(newAccount)

        val foundAccount = accountRepository.findById(savedAccount.userId)
        assertTrue(foundAccount.isPresent)
        assertEquals("new", foundAccount.get().username)
        assertEquals("new@test.com", foundAccount.get().email)
    }
}