package com.laplace.movie_review.dto.review

import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.Movie
import com.laplace.movie_review.entity.Review

data class ReviewCreateDTO(
    val email: String,
    val title: String,
    val rating: Float,
    val comment: String
)

fun ReviewCreateDTO.toReviewEntity(account: Account, movie: Movie): Review {
    return Review(account, movie, rating, comment)
}
