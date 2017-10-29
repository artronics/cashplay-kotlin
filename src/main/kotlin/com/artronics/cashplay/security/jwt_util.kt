package com.artronics.cashplay.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.Date
import kotlin.collections.HashMap

@Component
class JwtTokenUtil(
        @Value("\${jwt.secret}") private val secret: String,
        @Value("\${jwt.expiration}") private val expiration: Long

) : Serializable {
    val CLAIM_KEY_USERNAME = "sub"
    val CLAIM_KEY_AUDIENCE = "aud"
    val CLAIM_KEY_CREATED = "iat"

    fun getUsernameFromToken(token: String): String = this.getClaimFromToken(token, Claims::getSubject)
    fun getIssuedAtDateFromToken(token: String): Date = this.getClaimFromToken(token, Claims::getIssuedAt)
    fun getExpirationDateFromToken(token: String): Date = this.getClaimFromToken(token, Claims::getExpiration)
    fun getAudienceFromToken(token: String): String = this.getClaimFromToken(token, Claims::getAudience)

    fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims =
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(now())
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Date?) =
            created.before(lastPasswordReset)

    fun generateToken(userDetails: UserDetails) =
            doGenerateToken(getClaims(userDetails), userDetails.username, "web")

    private fun doGenerateToken(claims: MutableMap<String, Any?>, subject: String?, audience: String): String {
        val createdDate = now()
        val expirationDate = calculateExpirationDate(createdDate)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    private fun calculateExpirationDate(createdDate: Date) =
            Date(createdDate.time + expiration * 1000)

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val user = userDetails as JwtUser
        val username = getUsernameFromToken(token)
        val created = getIssuedAtDateFromToken(token)
        return (username == user.username
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate))
    }
}

fun getClaims(userDetails: UserDetails): MutableMap<String, Any?> {
    val claims = HashMap<String, Any?>()

    return claims
}

fun now() = Date()
