package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.UserInfo
import com.laplace.movie_review.entity.User
import com.laplace.movie_review.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class AuthController(
    private val authService: AuthService,
) {
    @GetMapping("/user")
    fun createUserPage(): String {
        return "users"
    }

    @PostMapping("/user")
    @ResponseBody
    fun registerUser(
        @RequestParam("userName") username: String,
        @RequestParam("email") email: String,
        @RequestParam("password") password: String
    ): String {
        authService.createUser(UserInfo(username, email, password))
        return "User registered: $username"
    }

    @GetMapping("/login")
    fun loginView(): String {
        return "login"
    }

    @PostMapping("/login")
    fun login(@RequestBody request: UserInfo): ResponseEntity<String> {
        val token = authService.login(request)
        return ResponseEntity.ok(token)
    }

//    @PostMapping("/logout")
//    @ResponseBody
//    fun logout(): String {
//        authService.logout()
//        return "redirect:/"
//    }
}