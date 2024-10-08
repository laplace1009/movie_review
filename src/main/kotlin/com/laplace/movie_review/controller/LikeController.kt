package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.like.LikeCreateDTO
import com.laplace.movie_review.service.LikeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping(("/like"))
class LikeController(private val likeService: LikeService) {
    @PostMapping("/create")
    fun addLike(
        @RequestParam accountId: Long,
        @RequestParam reviewId: Long
    ): ResponseEntity<Unit> {
        val likeCreateDTO = LikeCreateDTO(accountId, reviewId)
        likeService.createLike(likeCreateDTO)
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("/")).build()
    }
}