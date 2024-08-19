package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.review.ReviewCreateDTO
import com.laplace.movie_review.service.AccountService
import com.laplace.movie_review.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import java.net.URI

@RestController
@RequestMapping("/review")
class ReviewController(
    private val reviewService: ReviewService,
    private val accountService: AccountService
) {
    @GetMapping
    fun reviewMainTemplate(): ModelAndView {
        val view = ModelAndView("review/main")
        view.addObject("review", "Review")
        return view
    }

    @GetMapping("/create")
    fun createTemplate(): ModelAndView {
        val view = ModelAndView("review/create")
        return view
    }

    @PostMapping("/create")
    fun createReview(
        @RequestParam title: String,
        @RequestParam rating: Float,
        @RequestParam comment: String
    ): ResponseEntity<Unit> {
        val accountInfoDTO = accountService.getCurrentAccount()
        val reviewCreateDTO = ReviewCreateDTO(accountInfoDTO.email, title, rating, comment)
        reviewService.createReview(reviewCreateDTO)
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("/")).build()
    }
}