package com.artronics.cashplay.security

import com.artronics.cashplay.api.AuthorityName
import com.artronics.cashplay.api.AuthorityRepository
import com.artronics.cashplay.api.User
import com.artronics.cashplay.api.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationEventPublisherAware
import org.springframework.data.rest.core.event.BeforeSaveEvent
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AuthenticationController(
        private val authenticationManager: AuthenticationManager,
        private val jwtTokenUtil: JwtTokenUtil,
        private val userDetailsService: UserDetailsService,
        private val userRepository: UserRepository,
        private val authorityRepository: AuthorityRepository

) {

    @PostMapping(value = "\${jwt.route.authentication.path}/login")
    fun createAuthenticationToken(@RequestBody jwtAuthenticationRequest: JwtAuthenticationRequest)
            : ResponseEntity<JwtAuthenticationResponse> {

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        jwtAuthenticationRequest.username,
                        jwtAuthenticationRequest.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationRequest.username)
        val token = jwtTokenUtil.generateToken(userDetails)

        return ResponseEntity.ok(JwtAuthenticationResponse(token))
    }

    @PostMapping(value = "\${jwt.route.authentication.path}/register")
    fun register(@RequestBody user: User)
            : ResponseEntity<JwtAuthenticationResponse> {
        val admin = authorityRepository.findByName(AuthorityName.ROLE_ADMIN)!!
        val authorities = listOf(admin)
        user.authorities = authorities
        user.enabled = true
        user.lastPasswordResetDate = Date()

        val password = user.password
        val codec = BCryptPasswordEncoder()
        user.password = codec.encode(password)

        userRepository.save(user)

        val auth = JwtAuthenticationRequest(user.username!!, password!!)
        return createAuthenticationToken(auth)
    }
}

data class JwtAuthenticationRequest(var username: String? = null, var password: String? = null)
data class JwtAuthenticationResponse(val token: String)
