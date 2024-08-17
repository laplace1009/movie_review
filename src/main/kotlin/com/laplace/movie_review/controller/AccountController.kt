package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.account.AccountCreateDTO
import com.laplace.movie_review.dto.accountProvider.AccountProviderCreateDTO
import com.laplace.movie_review.service.AccountProviderService
import com.laplace.movie_review.service.AccountService
import com.laplace.movie_review.util.AuthProviderName
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class AccountController(
    private val accountService: AccountService,
    private val accountProviderService: AccountProviderService,
) {
    @GetMapping("/account")
    fun createAccountPage(): String {
        return "account"
    }

    @PostMapping("/account")
    @ResponseBody
    fun registerUser(
        @RequestParam("userName") username: String,
        @RequestParam("email") email: String,
        @RequestParam("password") password: String
    ): String {
        val accountId = accountService.createLocalUser(AccountCreateDTO(username, email, password))
        accountProviderService.createLocalProvider(AccountProviderCreateDTO(accountId, AuthProviderName.LOCAL.providerName, ""))
        return "User registered: $username"
    }

    @GetMapping("/login")
    fun loginPage(): String {
        return "login"
    }
}