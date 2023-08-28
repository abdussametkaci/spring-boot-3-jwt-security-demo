package com.example.springboot3jwtsecuritydemo.domain.dto.request

data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)
