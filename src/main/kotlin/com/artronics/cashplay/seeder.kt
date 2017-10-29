package com.artronics.cashplay


import com.artronics.cashplay.api.*
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class Seeder(
        private val userRepository: UserRepository,
        private val authorityRepository: AuthorityRepository

): ApplicationRunner {
    val cal = Calendar.getInstance()

    override fun run(p0: ApplicationArguments?) {
        createUsers()
    }

    fun createUsers(){
        val encoder = BCryptPasswordEncoder()
        val admin = Authority(null, null, AuthorityName.ROLE_ADMIN)
        val user = Authority(null, null, AuthorityName.ROLE_USER)

        authorityRepository.save(admin)
        authorityRepository.save(user)

        cal.add(Calendar.YEAR, -1)

        val jalal = User(
                firstName = "jalal",
                lastName = "hosseini",
                username = "jalalhosseiny@gmail.com",
                email = "jalalhosseiny@gmail.com",
                password = encoder.encode("secret"),
                enabled = true,
                authorities = listOf(admin, user),
                lastPasswordResetDate = cal.time
        )
        val reza = User(
                firstName = "reza",
                lastName = "hosseiny",
                username = "reza_21622@yahoo.co.uk",
                email = "reza_21622@yahoo.co.uk",
                password = encoder.encode("secret"),
                enabled = true,
                authorities = listOf(user),
                lastPasswordResetDate = cal.time
        )

        userRepository.save(jalal)
        userRepository.save(reza)
    }
}