package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.AccountProvider
import com.laplace.movie_review.util.AuthProviderName
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
class AccountProviderRepositoryTest {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountProviderRepository: AccountProviderRepository

    @BeforeEach
    fun beforeEach() {
        accountRepository.deleteAll()
        accountProviderRepository.deleteAll()

        val account = Account("test", "test@test.com", "1234")
        val accountProvider = AccountProvider(account, AuthProviderName.LOCAL.providerName, null)
        accountRepository.save(account)
        accountProviderRepository.save(accountProvider)
    }

    @Test
    fun `test findByAccount return when account exists`() {
        val account = accountRepository.findByEmail("test@test.com") as Account
        val accountProvider = accountProviderRepository.findByAccount(account)
        assertNotNull(accountProvider)
        assertEquals(AuthProviderName.LOCAL.providerName, accountProvider?.providerName)
    }

    @Test
    fun `test save`() {
        val newAccount = Account("test2", "test2@test.com", "1234")
        val savedAccount = accountRepository.save(newAccount)
        val newAccountProvider = AccountProvider(savedAccount, AuthProviderName.LOCAL.providerName, null)
        val savedAccountProvider = accountProviderRepository.save(newAccountProvider)
        assertNotNull(savedAccountProvider)
        assertEquals(savedAccountProvider.providerId, newAccountProvider.providerId)
    }

}