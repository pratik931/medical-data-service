package com.example.medicaldataservice.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class BloodPressure(
    @Column(name = "systolic_pressure")
    val systolic: Int? = null,

    @Column(name = "diastolic_pressure")
    val diastolic: Int? = null
)