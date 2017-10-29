package com.artronics.cashplay.security

import com.artronics.cashplay.api.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsServiceImpl(
        private val userRepository: UserRepository

) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByUsername(
                username ?: throw NullPointerException("username is null"))
                ?: throw UsernameNotFoundException("No user found with username $username .")

        return JwtUserFactory.create(user)
    }
}
