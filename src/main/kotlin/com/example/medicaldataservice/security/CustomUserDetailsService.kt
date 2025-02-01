package com.example.medicaldataservice.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val passwordEncoder: PasswordEncoder) : UserDetailsService {

    private val users: List<ApplicationUser> by lazy {
        val usersJson = System.getenv("APP_USERS") ?: System.getProperty("APP_USERS")
            ?: throw IllegalStateException("No users configured")

        jacksonObjectMapper().readValue<List<ApplicationUser>>(usersJson).map {
            it.copy(password = passwordEncoder.encode(it.password))
        }
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = users.find { it.username == username }
            ?: throw UsernameNotFoundException("User '$username' not found")

        return User(user.username, user.password, user.roles.map { SimpleGrantedAuthority("ROLE_$it") })
    }
}