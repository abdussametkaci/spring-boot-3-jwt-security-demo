package com.example.springboot3jwtsecuritydemo.repository

import com.example.springboot3jwtsecuritydemo.domain.model.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface TokenRepository : JpaRepository<Token, UUID> {

    @Query("""
            SELECT t
            FROM Token t
            INNER JOIN User u ON t.user.id = u.id
            WHERE u.id = :userId
            AND (t.expired = FALSE OR t.revoked = FALSE)
    """)
    fun findAllValidTokensByUserId(userId: UUID): List<Token>

    fun findByToken(token: String): Token?
}
