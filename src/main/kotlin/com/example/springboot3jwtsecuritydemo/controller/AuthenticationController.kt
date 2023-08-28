package com.example.springboot3jwtsecuritydemo.controller

import com.example.springboot3jwtsecuritydemo.domain.dto.request.AuthenticationRequest
import com.example.springboot3jwtsecuritydemo.domain.dto.request.RegisterRequest
import com.example.springboot3jwtsecuritydemo.domain.dto.response.AuthenticationResponse
import com.example.springboot3jwtsecuritydemo.service.AuthenticationService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): AuthenticationResponse {
        return authenticationService.register(request)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): AuthenticationResponse {
        return authenticationService.authenticate(request)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        authenticationService.refreshToken(request, response)
    }
}
