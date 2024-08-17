package com.laplace.movie_review.service

import com.laplace.movie_review.dto.review.ReviewCreateDTO
import com.laplace.movie_review.dto.review.toReviewEntity
import com.laplace.movie_review.entity.Review
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
    fun createReview(reviewCreateDTO: ReviewCreateDTO): Review {
        val account = accountRepository.findById(reviewCreateDTO.accountId).orElseThrow {
            throw IllegalStateException("Not exist account by ${reviewCreateDTO.accountId}Id")
        }

        val movie = movieRepository.findById(reviewCreateDTO.movieId).orElseThrow {
            throw IllegalStateException("Not exist movie by ${reviewCreateDTO.movieId}Id")
        }

        val review = reviewCreateDTO.toReviewEntity(account, movie)

        return reviewRepository.save(review)
    }

    @Transactional
    fun deleteReview(reviewId: Long) {
        val review = reviewRepository.findById(reviewId).orElseThrow {
            throw IllegalStateException("Not exist review by ${reviewId}Id")
        }
        reviewRepository.delete(review)
    }
}