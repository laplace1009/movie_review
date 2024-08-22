package com.laplace.movie_review.service

import com.laplace.movie_review.dto.account.AccountCreateDTO
import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.repository.AccountRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import kotlin.test.assertEquals

@SpringBootTest
class AccountServiceTest {
    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var accountService: AccountService

    lateinit var username: String
    lateinit var password: String
    lateinit var email: String

    @BeforeEach
    fun setUp() {
        username = "test"
        password = "1234"
        email = "test@test.com"
    }

    @Test
    fun `loadUserByUsername should return UserDetails when user exists`() {
        val account = Account(username, email, password)
        `when`(accountRepository.findByEmail(email)).thenReturn(account)

        val userDetails = accountService.loadUserByUsername(email)

        assertEquals(email, userDetails.username)
        assertEquals(password, userDetails.password)
        assertEquals(listOf("ROLE_ADMIN"), userDetails.authorities.map {it.authority })
    }

    @Test
    fun `loadUserByUsername should throw exception when user does not exists`() {
        `when`(accountRepository.findByEmail(email)).thenReturn(null)

        assertThrows<UsernameNotFoundException> { accountService.loadUserByUsername(email) }
    }

    @Test
    fun `createLocalUser should create a new if email does not exists`() {
        val accountCreateDTO = AccountCreateDTO(username, email, password)
        `when`(accountRepository.findByEmail(accountCreateDTO.email)).thenReturn(null)
        `when`(passwordEncoder.encode(accountCreateDTO.password)).thenReturn("encode")
        val savedAccount = Account(username, email, "encode").apply {
            id = 1
        }
        `when`(accountRepository.save(any(Account::class.java))).thenReturn(savedAccount)
        val userId = accountService.createLocalUser(accountCreateDTO)

        assertEquals(1L, userId)

        verify(accountRepository).save(any(Account::class.java))
    }

    @Test
    @WithMockUser(username = "test@test.com")
    fun `getCurrentAccount should return authenticated user's info`() {
        val authentication = UsernamePasswordAuthenticationToken(
            User(email, password, emptyList()), null, emptyList()
        )

        SecurityContextHolder.getContext().authentication = authentication

        val accountInfoDTO = accountService.getCurrentAccount()

        assertEquals(email, accountInfoDTO.email)
    }
}