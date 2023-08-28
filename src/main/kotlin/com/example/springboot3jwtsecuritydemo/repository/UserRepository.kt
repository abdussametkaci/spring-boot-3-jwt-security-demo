package com.example.springboot3jwtsecuritydemo.repository

import com.example.springboot3jwtsecuritydemo.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String): User?
}
