package com.example.medicaldataservice.security

data class ApplicationUser(
    val username: String,
    val password: String,
    val roles: List<String>
)