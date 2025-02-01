package com.example.medicaldataservice.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class MedicalDataRequest(
    @field:NotBlank(message = "Patient ID is required")
    val patientId: String,

    @field:Valid
    val bloodPressure: BloodPressureRequest?,

    @field:Min(value = 0, message = "Heartbeat rate should be at least 0")
    @field:Max(value = 300, message = "Heartbeat rate should not exceed 300")
    val heartbeatRate: Int,
)