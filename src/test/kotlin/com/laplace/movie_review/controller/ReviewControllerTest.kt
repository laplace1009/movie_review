package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.account.AccountInfoDTO
import com.laplace.movie_review.dto.review.ReviewCreateDTO
import com.laplace.movie_review.service.AccountService
import com.laplace.movie_review.service.ReviewService
import com.laplace.movie_review.service.TokenService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.util.UriComponentsBuilder

@WebMvcTest(ReviewController::class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var reviewService: ReviewService

    @MockBean
    private lateinit var tokenService: TokenService

    @MockBean
    private lateinit var accountService: AccountService
    @Test
    fun `GET review should return review view`() {
        mockMvc.perform(get("/review/main"))
            .andExpect { status().isOk; }
    }

    @Test
    fun `POST review should return review view`() {
        val accountInfoDTO = AccountInfoDTO("test@example.com")

        Mockito.`when`(accountService.getCurrentUser()).thenReturn(accountInfoDTO)

        val uri = UriComponentsBuilder.fromUriString("/review/create")
            .queryParam("title", "Test Title")
            .queryParam("rating", 4.5)
            .queryParam("comment", "Test Comment")
            .build()
            .toUri()

        mockMvc.perform(post(uri))
            .andExpect {
                status().isSeeOther
                header().string("Location", "/")
            }


        Mockito.verify(reviewService).createReview(
            ReviewCreateDTO(
                accountInfoDTO.email,
                "Test Title",
                4.5f,
                "Test Comment"
            )
        )
    }
}