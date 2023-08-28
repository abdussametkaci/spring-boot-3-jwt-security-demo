package com.example.springboot3jwtsecuritydemo.domain.model

import com.example.springboot3jwtsecuritydemo.domain.enums.TokenType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity
data class Token(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val token: String,

    @Enumerated(EnumType.STRING)
    val tokenType: TokenType,

    var expired: Boolean,
    var revoked: Boolean,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User
)
