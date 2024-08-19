package com.laplace.movie_review.service

import com.laplace.movie_review.dto.accountProvider.AccountProviderCreateDTO
import com.laplace.movie_review.dto.accountProvider.toEntity
import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.AccountProvider
import com.laplace.movie_review.repository.AccountProviderRepository
import com.laplace.movie_review.repository.AccountRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
class AccountProviderTest {
    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var accountProviderRepository: AccountProviderRepository

    @InjectMocks
    private lateinit var accountProviderService: AccountProviderService

    @Test
    fun `createLocalProvider should return AccountProvider`() {
        val account = Account("test", "test@test.com", "1234").apply { userId = 1L }
        val accountProviderCreateDTO = AccountProviderCreateDTO(1L, "LOCAL", "")
        val accountProvider = accountProviderCreateDTO.toEntity(account).apply {
            id = 1L
        }
        `when`(accountRepository.findById(accountProviderCreateDTO.accountId)).thenReturn(Optional.of(account))
        `when`(accountProviderRepository.save(any(AccountProvider::class.java))).thenReturn(accountProvider)
        val provider = accountProviderService.createLocalProvider(accountProviderCreateDTO)
        assertEquals(provider.providerName, "LOCAL")
        assertEquals(provider.id, 1L)
    }

    @Test
    fun `createLocalProvider should throw exception when account does not found`() {
        val accountProviderCreateDTO = AccountProviderCreateDTO(1L, "LOCAL", "")

        `when`(accountProviderRepository.findById(accountProviderCreateDTO.accountId)).thenReturn(Optional.empty())

        val exception = assertThrows(IllegalStateException::class.java) {
            accountProviderService.createLocalProvider(accountProviderCreateDTO)
        }

        assertEquals("Invalid Account ID: 1", exception.message)
    }
}