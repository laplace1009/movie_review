package com.laplace.movie_review.controller

import com.laplace.movie_review.service.ReviewService
import com.laplace.movie_review.service.TokenService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ReviewController::class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var reviewService: ReviewService

    @MockBean
    private lateinit var tokenService: TokenService

    @Test
    fun `GET review should return review view`() {
        mockMvc.perform(get("/review/main"))
            .andExpect { status().isOk; }
    }
}