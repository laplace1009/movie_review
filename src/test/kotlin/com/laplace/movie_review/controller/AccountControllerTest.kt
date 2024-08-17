package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.account.AccountCreateDTO
import com.laplace.movie_review.entity.AccountProvider
import com.laplace.movie_review.service.AccountProviderService
import com.laplace.movie_review.service.AccountService
import com.laplace.movie_review.service.TokenService
import com.laplace.movie_review.util.AuthProviderName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(AccountController::class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var accountService: AccountService

    @MockBean
    private lateinit var tokenService: TokenService

    @MockBean
    private lateinit var accountProviderService: AccountProviderService

    @Test
    fun `GET account should return user view`() {
        mockMvc.perform(get("/account"))
            .andExpect{
                status().isOk
                view().name("account")
            }
    }

    @Test
    fun `POST account should return user view`() {
        val username = "username"
        val email = "test@gmail.com"
        val password = "1234"


        Mockito.`when`(accountService.createLocalUser(AccountCreateDTO(username, email, password))).thenReturn(1)

        mockMvc.perform(
            post("/account")
                .param("username", username)
                .param("email", email)
                .param("password", password)
        ).andExpect {
            status().isOk
            content().string("User registered: $username")
        }
    }

    @Test
    fun `GET login should return login view`() {
        mockMvc.perform(get("/login"))
            .andExpect{
                status().isOk
                view().name("login")
            }
    }
}
