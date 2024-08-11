package com.laplace.movie_review.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/home")
    fun home(model: Model, @AuthenticationPrincipal user: OAuth2User): String {
        model.addAttribute("name", user.getAttribute<String>("name"))
        return "home"
    }
}