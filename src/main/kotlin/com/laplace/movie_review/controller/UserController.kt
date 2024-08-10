package com.laplace.movie_review.controller

import com.laplace.movie_review.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class UserController(private val userService: UserService) {
    @GetMapping("/")
    fun mainIndex(): String {
        return "index"
    }

    @GetMapping("/users")
    fun createUserPage(): String {
        return "users"
    }

    @PostMapping("/users")
    @ResponseBody
    fun registerUser(
        @RequestParam("userName") username: String,
        @RequestParam("email") email: String,
        @RequestParam("password") password: String
    ): String {
        val retValue = userService.createUser(username, email, password)
        return "User registered: $username"
    }

    @GetMapping("/login")
    fun loginPage(): String {
        return "login"
    }

    @PostMapping("/login")
    @ResponseBody
    fun login(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String
    ): String {
        val retVal = userService.loginUser(email, password)
        return "User logged: $email"
    }
}