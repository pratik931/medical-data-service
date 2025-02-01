package com.example.medicaldataservice.dto

import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val code: Int,
    val identifier: String,
    val message: String,
    val path: String? = null
)