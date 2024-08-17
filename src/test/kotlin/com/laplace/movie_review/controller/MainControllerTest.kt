package com.laplace.movie_review.controller

import com.laplace.movie_review.service.AccountService
import com.laplace.movie_review.service.TokenService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(MainController::class)
class MainControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var accountService: AccountService

    @MockBean
    private lateinit var tokenService: TokenService

    @Test
    fun `GET index should return index page`() {
        mockMvc.perform(get("/"))
            .andExpect {
                status().isOk
            }
    }
}