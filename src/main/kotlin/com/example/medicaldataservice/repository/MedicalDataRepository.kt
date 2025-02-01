package com.example.medicaldataservice.repository

import com.example.medicaldataservice.domain.MedicalData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MedicalDataRepository : JpaRepository<MedicalData, UUID> {
    fun findByPatientId(patientId: String): List<MedicalData>
}