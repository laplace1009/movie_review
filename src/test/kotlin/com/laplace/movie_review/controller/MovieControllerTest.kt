package com.laplace.movie_review.controller

import com.laplace.movie_review.service.MovieService
import com.laplace.movie_review.service.TokenService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(MovieController::class)
@AutoConfigureMockMvc(addFilters = false)
class MovieControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var movieService: MovieService

    @MockBean
    private lateinit var tokenService: TokenService


    @Test
    fun `GET movie should return movie view`() {
        mockMvc.perform(get("/movie"))
            .andExpect {
                status().isOk
            }
    }

    @Test
    fun `POST movie create should return movie`() {
        val title = "dune"
        val description = "dune dune dune dune dune"
        val director = "dev"
        val releaseDate = "2024-01-01"
        mockMvc.perform(post("/movie/create")
            .param("title", title)
            .param("description", description)
            .param("director", director)
            .param("releaseDate", releaseDate)
        ).andExpect {
            status().isOk
            content().string("create ${title} review successful")
        }

    }
}