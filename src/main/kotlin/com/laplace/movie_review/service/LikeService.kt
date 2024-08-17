package com.laplace.movie_review.service

import com.laplace.movie_review.dto.like.LikeCreateDTO
import com.laplace.movie_review.dto.like.toEntity
import com.laplace.movie_review.entity.Like
import com.laplace.movie_review.repository.AccountRepository
import com.laplace.movie_review.repository.LikeRepository
import com.laplace.movie_review.repository.ReviewRepository
import org.springframework.stereotype.Service

@Service
class LikeService(
    private val accountRepository: AccountRepository,
    private val reviewRepository: ReviewRepository,
    private val likeRepository: LikeRepository
) {
    fun createLike(likeCreateDTO: LikeCreateDTO): Like {
        val account = accountRepository.findById(likeCreateDTO.accountId).orElseThrow {
            throw IllegalStateException("Invalid account id: ${likeCreateDTO.accountId}")
        }

        val review = reviewRepository.findById(likeCreateDTO.reviewId).orElseThrow {
            throw IllegalStateException("Invalid review id: ${likeCreateDTO.reviewId}")
        }

        val like = likeRepository.save(likeCreateDTO.toEntity(account, review))

        return like
    }
}