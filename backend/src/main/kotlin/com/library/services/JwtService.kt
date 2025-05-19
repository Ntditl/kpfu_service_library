package com.library.services

import com.library.models.UserDTO
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*

class JwtService {
    private val secret = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val issuer = "library-app"
    private val validityInMs = 36_000_00 * 24 // 24 часа

    fun generateToken(user: UserDTO): String {
        val now = Date()
        val expiration = Date(now.time + validityInMs)

        return Jwts.builder()
            .setSubject(user.user_id.toString())
            .setIssuedAt(now)
            .setExpiration(expiration)
            .setIssuer(issuer)
            .signWith(secret)
            .compact()
    }

    fun validateToken(token: String): Int {
        return Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
            .toInt()
    }
} 