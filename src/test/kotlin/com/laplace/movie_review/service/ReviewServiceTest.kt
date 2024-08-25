package com.laplace.movie_review.service

import com.laplace.movie_review.dto.review.ReviewCreateDTO
import com.laplace.movie_review.dto.review.toEntity
import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.Genre
import com.laplace.movie_review.entity.Movie
import com.laplace.movie_review.entity.Review
import com.laplace.movie_review.repository.AccountRepository
import com.laplace.movie_review.repository.MovieRepository
import com.laplace.movie_review.repository.ReviewRepository
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach

@SpringBootTest
class ReviewServiceTest {
    @Mock
    lateinit var reviewRepository: ReviewRepository

    @Mock
    lateinit var accountRepository: AccountRepository

    @Mock
    lateinit var movieRepository: MovieRepository

    @InjectMocks
    lateinit var reviewService: ReviewService

    private lateinit var reviewCreateDTO: ReviewCreateDTO
    private lateinit var storedAccount: Account
    private lateinit var storedMovie: Movie

    @BeforeEach
    fun setUp() {
        reviewCreateDTO = ReviewCreateDTO("test@test.com", "dune", 4.5f, "test test")
        storedAccount = Account("test","test@test.com", "1234")
        storedMovie = Movie("dune", "dune dune", "dev", "2024-01-01").apply {
            genres = setOf(Genre("action"), Genre("sf"))
        }
    }

    @Test
    fun `createReview should return Review`() {
        val reviewCreateDTO = ReviewCreateDTO("test@test.com", "dune", 4.5f, "test test")
        val storedAccount = Account("test","test@test.com", "1234")
        val storedMovie = Movie("dune", "dune dune", "dev", "2024-01-01").apply {
            genres = setOf(Genre("action"), Genre("sf"))
        }
        val review = reviewCreateDTO.toEntity(storedAccount, storedMovie)
        `when`(accountRepository.findByEmail(reviewCreateDTO.email)).thenReturn(storedAccount)
        `when`(movieRepository.findMovieByTitle(reviewCreateDTO.title)).thenReturn(storedMovie)
        `when`(reviewRepository.save(any(Review::class.java))).thenReturn(review)

        val savedReview = reviewService.createReview(reviewCreateDTO)

        assertEquals(savedReview.title, "dune")
        assertEquals(savedReview.email, "test@test.com")
        assertEquals(savedReview.rating, 4.5f)
    }

    @Test
    fun `createReview should return empty account`() {
        `when`(accountRepository.findByEmail(reviewCreateDTO.email)).thenReturn(null)

        val exception = assertThrows(IllegalStateException::class.java) {
            reviewService.createReview(reviewCreateDTO)
        }

        assertEquals(exception.message, "Not found account by ${reviewCreateDTO.email}")
    }

    @Test
    fun `createReview should return empty movie`() {
        `when`(accountRepository.findByEmail(reviewCreateDTO.email)).thenReturn(storedAccount)
        `when`(movieRepository.findMovieByTitle(reviewCreateDTO.title)).thenReturn(null)

        val exception = assertThrows(IllegalStateException::class.java) {
            reviewService.createReview(reviewCreateDTO)
        }

        assertEquals(exception.message, "Not found movie by ${reviewCreateDTO.title}")
    }
}