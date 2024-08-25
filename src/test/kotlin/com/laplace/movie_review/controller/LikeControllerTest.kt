package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.like.LikeCreateDTO
import com.laplace.movie_review.dto.like.toEntity
import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.Movie
import com.laplace.movie_review.entity.Review
import com.laplace.movie_review.service.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class LikeControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var likeService: LikeService

    @MockBean
    private lateinit var tokenService: TokenService
    @Test
    fun `addLike should return CREATED status`() {
        val accountId = 1L
        val reviewId = 2L
        val likeCreateDTO = LikeCreateDTO(accountId, reviewId)

        val account = Account("test", "test@gmail.com", "1234")
        val movie = Movie("dune", "dune dune", "dev", "2024-01-01")
        val review = Review(account, movie, 4.5f, "good movie")

        Mockito.`when`(likeService.createLike(likeCreateDTO)).thenReturn(likeCreateDTO)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/like")
                .param("accountId", accountId.toString())
                .param("reviewId", reviewId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect {
                MockMvcResultMatchers.status().isCreated
                MockMvcResultMatchers.jsonPath("$.accountId").value(accountId)
                MockMvcResultMatchers.jsonPath("$.reviewId").value(reviewId)
            }

    }
}