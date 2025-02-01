package com.example.medicaldataservice.mapper

import com.example.medicaldataservice.domain.BloodPressure
import com.example.medicaldataservice.domain.MedicalData
import com.example.medicaldataservice.dto.*

object MedicalDataMapper {

    fun toEntity(request: MedicalDataRequest): MedicalData {
        return MedicalData(
            patientId = request.patientId,
            bloodPressure = request.bloodPressure?.let { toEntity(it) },
            heartbeatRate = request.heartbeatRate
        )
    }

    private fun toEntity(request: BloodPressureRequest): BloodPressure {
        return BloodPressure(
            systolic = request.systolic,
            diastolic = request.diastolic
        )
    }

    fun toResponse(entity: MedicalData): MedicalDataResponse {
        return MedicalDataResponse(
            id = entity.id.toString(),
            patientId = entity.patientId,
            bloodPressure = entity.bloodPressure?.let { toResponse(it) },
            heartbeatRate = entity.heartbeatRate,
            createdAt = entity.createdAt!! // createdAt is never null due to @CreationTimestamp
        )
    }

    private fun toResponse(entity: BloodPressure): BloodPressureResponse {
        return BloodPressureResponse(
            systolic = entity.systolic,
            diastolic = entity.diastolic
        )
    }

    fun toCreateResponse(entity: MedicalData): MedicalDataCreateResponse {
        return MedicalDataCreateResponse(
            id = entity.id.toString()
        )
    }
}