package com.example.springboot3jwtsecuritydemo.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date

@Service
class JwtService {

    fun getUserName(token: String): String? {
        return getClaims(token).subject
    }

    fun getExpiration(token: String): Date {
        return getClaims(token).expiration
    }

    fun getClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = getUserName(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean {
        return getExpiration(token).before(Date())
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return buildToken(extraClaims, userDetails, JWT_EXPIRATION)
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        return buildToken(emptyMap(), userDetails, REFRESH_EXPIRATION)
    }

    private fun buildToken(extraClaims: Map<String, Any>, userDetails: UserDetails, expiration: Long): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private companion object {
        const val SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
        const val JWT_EXPIRATION = 86400000L // 1000 * 60 * 60 * 24 // one day
        const val REFRESH_EXPIRATION = 604800000L // 7 days
    }
}
