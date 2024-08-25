package com.laplace.movie_review.service

import com.laplace.movie_review.dto.like.LikeCreateDTO
import com.laplace.movie_review.dto.like.toEntity
import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.Like
import com.laplace.movie_review.entity.Movie
import com.laplace.movie_review.entity.Review
import com.laplace.movie_review.repository.AccountRepository
import com.laplace.movie_review.repository.LikeRepository
import com.laplace.movie_review.repository.ReviewRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
class LikeServiceTest {
    @Mock
    lateinit var accountRepository: AccountRepository

    @Mock
    lateinit var reviewRepository: ReviewRepository

    @Mock
    lateinit var likeRepository: LikeRepository

    @InjectMocks
    lateinit var likeService: LikeService

    private lateinit var storedAccount: Account
    private lateinit var storedReview: Review
    private lateinit var storedMovie: Movie
    private lateinit var likeCreateDTO: LikeCreateDTO

    @BeforeEach
    fun setUp() {
        storedAccount = Account("test", "test@test.com", "1234").apply { id = 1L }
        storedMovie = Movie("dune", "dune dune", "dev", "2024-01-01").apply { id = 1L }
        storedReview = Review(storedAccount, storedMovie, 4.5f, "test test").apply { id = 1L }
        likeCreateDTO = LikeCreateDTO(1L, 1L)
    }

    @Test
    fun `createLike should return Like`() {
        val like = likeCreateDTO.toEntity(storedAccount, storedReview)
        `when`(accountRepository.findById(storedAccount.id)).thenReturn(Optional.of(storedAccount))
        `when`(reviewRepository.findById(storedReview.id)).thenReturn(Optional.of(storedReview))
        `when`(likeRepository.save(any(Like::class.java))).thenReturn(like)

        val savedLike = likeService.createLike(likeCreateDTO)

        assertEquals(savedLike.reviewId, 1L)
    }

    @Test
    fun `createLike should return empty review by account is null`() {
        `when`(accountRepository.findById(storedAccount.id)).thenReturn(Optional.empty())

        val exception = assertThrows(IllegalStateException::class.java) {
            likeService.createLike(likeCreateDTO)
        }

        assertEquals(exception.message, "Invalid account id: ${likeCreateDTO.accountId}")
    }

    @Test
    fun `createLike should return empty review by review is null`() {
        `when`(accountRepository.findById(storedAccount.id)).thenReturn(Optional.of(storedAccount))
        `when`(reviewRepository.findById(storedReview.id)).thenReturn(Optional.empty())

        val exception = assertThrows(IllegalStateException::class.java) {
            likeService.createLike(likeCreateDTO)
        }

        assertEquals(exception.message, "Invalid review id: ${likeCreateDTO.reviewId}")
    }
}