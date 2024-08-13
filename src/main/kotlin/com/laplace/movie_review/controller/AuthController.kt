package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.AccountInfo
import com.laplace.movie_review.service.AuthService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import util.AuthProviderName

@Controller
class AuthController(
    private val authService: AuthService,
) {
    @GetMapping("/account")
    fun createUserPage(): String {
        return "user"
    }

    @PostMapping("/account")
    @ResponseBody
    fun registerUser(
        @RequestParam("userName") username: String,
        @RequestParam("email") email: String,
        @RequestParam("password") password: String
    ): String {
        authService.createLocalUser(AccountInfo(username, email, password, AuthProviderName.LOCAL, providerId = null))
        return "User registered: $username"
    }

    @GetMapping("/login")
    fun loginView(): String {
        return "login"
    }

    @PostMapping("/login")
    @ResponseBody
    fun login(@RequestParam email: String, @RequestParam password: String): String {
        return "login success"
    }
}