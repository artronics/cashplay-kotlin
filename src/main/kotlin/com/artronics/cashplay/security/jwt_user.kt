package com.artronics.cashplay.security

import com.artronics.cashplay.api.Authority
import com.artronics.cashplay.api.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

class JwtUser(
        @JsonIgnore
        val id: Long?,
        val email: String?,
        val firstName: String?,
        val lastName: String?,
        @JsonIgnore
        val lastPasswordResetDate: Date?,
        password: String?,
        username: String?,
        authorities: MutableCollection<out GrantedAuthority>,
        enabled: Boolean?

) : UserDetails {

    private val username = username
        get

    private val password = password
        @JsonIgnore
        get

    private val authorities = authorities
        get

    private val enabled = enabled
        @JsonIgnore
        get

    override fun getUsername(): String = username!!

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    @JsonIgnore
    override fun isEnabled(): Boolean = enabled!!

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @JsonIgnore
    override fun getPassword(): String = password!!

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true
}

class JwtUserFactory {
    companion object {
        fun create(user: User) = JwtUser(
                id = user.id,
                password = user.password,
                username = user.username,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                enabled = user.enabled,
                lastPasswordResetDate = user.lastPasswordResetDate,
                authorities = mapToGrantedAuthorities(user.authorities)
        )

        private fun mapToGrantedAuthorities(authorities: List<Authority>?) = authorities!!
                .stream()
                .map({ authority -> SimpleGrantedAuthority(authority.name?.name) })
                .collect(Collectors.toList())
    }
}

