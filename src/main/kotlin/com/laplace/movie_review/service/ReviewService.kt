package com.laplace.movie_review.service

import com.laplace.movie_review.dto.review.ReviewCreateDTO
import com.laplace.movie_review.dto.review.toEntity
import com.laplace.movie_review.repository.AccountRepository
import com.laplace.movie_review.repository.MovieRepository
import com.laplace.movie_review.repository.ReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    private val accountRepository: AccountRepository,
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
) {
    @Transactional
    fun createReview(reviewCreateDTO: ReviewCreateDTO): ReviewCreateDTO {
        val account = accountRepository.findByEmail(reviewCreateDTO.email)
            ?: throw IllegalStateException("Not found account by ${reviewCreateDTO.email}")

        val movie = movieRepository.findMovieByTitle(reviewCreateDTO.title)
            ?: throw IllegalStateException("Not found movie by ${reviewCreateDTO.title}")

        val review = reviewCreateDTO.toEntity(account, movie)

        return reviewRepository.save(review).toDTO()
    }

    @Transactional
    fun deleteReview(reviewId: Long) {
        val review = reviewRepository.findById(reviewId).orElseThrow {
            throw IllegalStateException("Not exist review by ${reviewId}Id")
        }
        reviewRepository.delete(review)
    }
}