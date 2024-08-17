package com.laplace.movie_review.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/like")
class LikeController {
    @PostMapping
    fun addLike(
        @RequestParam accountId: Long,
        @RequestParam reviewId: Long
    ) {

    }
}