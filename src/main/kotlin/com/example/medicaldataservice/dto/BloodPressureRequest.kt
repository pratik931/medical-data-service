package com.example.medicaldataservice.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class BloodPressureRequest(
    @field:Min(value = 0, message = "Systolic pressure should be at least 0")
    @field:Max(value = 300, message = "Systolic pressure should not exceed 300")
    val systolic: Int?,

    @field:Min(value = 0, message = "Diastolic pressure should be at least 0")
    @field:Max(value = 200, message = "Diastolic pressure should not exceed 200")
    val diastolic: Int?
)