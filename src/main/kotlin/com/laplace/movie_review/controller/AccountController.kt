package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.account.AccountLocalCreateDTO
import com.laplace.movie_review.dto.accountProvider.AccountProviderCreateDTO
import com.laplace.movie_review.service.AccountProviderService
import com.laplace.movie_review.service.AccountService
import com.laplace.movie_review.util.AuthProviderName
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI

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
    ): ResponseEntity<Unit> {
        val accountLocalCreateDTO = AccountLocalCreateDTO(username, email, password)
        val accountId = accountService.createLocalUser(accountLocalCreateDTO)
        accountProviderService.createLocalProvider(AccountProviderCreateDTO(accountId, AuthProviderName.LOCAL.providerName, ""))
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI("/login")).build()
    }

    @GetMapping("/login")
    fun loginPage(): String {
        return "login"
    }
}