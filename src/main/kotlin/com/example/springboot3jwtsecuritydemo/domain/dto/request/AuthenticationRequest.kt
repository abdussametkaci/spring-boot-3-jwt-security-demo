package com.example.springboot3jwtsecuritydemo.domain.dto.request

data class AuthenticationRequest(
    val email: String,
    val password: String
)
