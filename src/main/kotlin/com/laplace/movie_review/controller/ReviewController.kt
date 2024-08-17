package com.laplace.movie_review.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/review")
class ReviewController {
    @GetMapping
    fun reviewMainTemplate(): ModelAndView {
        val view = ModelAndView("review/main")
        view.addObject("review", "Review")
        return view
    }
}