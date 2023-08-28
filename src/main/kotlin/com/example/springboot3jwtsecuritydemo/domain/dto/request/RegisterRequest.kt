package com.example.springboot3jwtsecuritydemo.domain.dto.request

import com.example.springboot3jwtsecuritydemo.domain.enums.Role

data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val role: Role
)
