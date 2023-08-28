package com.example.springboot3jwtsecuritydemo.service

import com.example.springboot3jwtsecuritydemo.domain.dto.request.AuthenticationRequest
import com.example.springboot3jwtsecuritydemo.domain.dto.request.RegisterRequest
import com.example.springboot3jwtsecuritydemo.domain.dto.response.AuthenticationResponse
import com.example.springboot3jwtsecuritydemo.domain.enums.Role
import com.example.springboot3jwtsecuritydemo.domain.enums.TokenType
import com.example.springboot3jwtsecuritydemo.domain.model.Token
import com.example.springboot3jwtsecuritydemo.domain.model.User
import com.example.springboot3jwtsecuritydemo.repository.TokenRepository
import com.example.springboot3jwtsecuritydemo.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val tokenRepository: TokenRepository
) {

    fun register(request: RegisterRequest): AuthenticationResponse {
        val user = with(request) {
            User(
                name = name,
                surname = surname,
                email = email,
                password = passwordEncoder.encode(password),
                role = Role.USER
            )
        }

        val savedUser = userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)
        saveUserToken(savedUser, jwtToken)
        return AuthenticationResponse(jwtToken, refreshToken)
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        with(request) {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))

            val user = userRepository.findByEmail(email)!!
            val jwtToken = jwtService.generateToken(user)
            val refreshToken = jwtService.generateRefreshToken(user)
            revokeAllUserTokens(user)
            saveUserToken(user, jwtToken)
            return AuthenticationResponse(jwtToken, refreshToken)
        }
    }

    private fun saveUserToken(user: User, jwtToken: String) {
        val token = Token(
            user = user,
            token = jwtToken,
            tokenType = TokenType.BEARER,
            expired = false,
            revoked = false
        )
        tokenRepository.save(token)
    }

    private fun revokeAllUserTokens(user: User) {
        val validUserTokens = tokenRepository.findAllValidTokensByUserId(user.id!!)

        if (validUserTokens.isEmpty()) return

        validUserTokens.map { token ->
            token.apply {
                expired = true
                revoked = true
            }
        }

        tokenRepository.saveAll(validUserTokens)
    }

    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return

        val refreshToken = authHeader.substring(7)
        val userEmail = jwtService.getUserName(refreshToken)
        if (userEmail != null) {
            val user = userRepository.findByEmail(userEmail) ?: throw Exception()
            if (jwtService.isTokenValid(refreshToken, user)) {
                val accessToken = jwtService.generateToken(user)
                revokeAllUserTokens(user)
                saveUserToken(user, accessToken)
                val authResponse = AuthenticationResponse(accessToken, refreshToken)
                ObjectMapper().writeValue(response.outputStream, authResponse)
            }
        }
    }
}

