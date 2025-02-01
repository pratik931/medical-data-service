package com.example.medicaldataservice.dto

import java.time.LocalDateTime

data class MedicalDataResponse(
    val id: String,
    val patientId: String,
    val bloodPressure: BloodPressureResponse?,
    val heartbeatRate: Int?,
    val createdAt: LocalDateTime
)