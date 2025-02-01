package com.example.medicaldataservice.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "medical_data")
data class MedicalData(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(name = "patient_id", nullable = false, updatable = false)
    val patientId: String,

    @Embedded
    val bloodPressure: BloodPressure? = null,

    @Column(name = "heartbeat_rate")
    val heartbeatRate: Int? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    val createdAt: LocalDateTime? = null
)