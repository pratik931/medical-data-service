package com.example.medicaldataservice.service

import com.example.medicaldataservice.dto.MedicalDataCreateResponse
import com.example.medicaldataservice.dto.MedicalDataRequest
import com.example.medicaldataservice.dto.MedicalDataResponse
import com.example.medicaldataservice.mapper.MedicalDataMapper
import com.example.medicaldataservice.repository.MedicalDataRepository
import com.example.medicaldataservice.util.ValidationUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.List

@Service
class MedicalDataService(
    private val medicalDataRepository: MedicalDataRepository
) {
    @Transactional
    fun saveMedicalData(medicalDataRequest: MedicalDataRequest): MedicalDataCreateResponse {
        ValidationUtil.validate(medicalDataRequest)

        val savedMedicalData = medicalDataRepository.save(MedicalDataMapper.toEntity(medicalDataRequest))
        return MedicalDataMapper.toCreateResponse(savedMedicalData)
    }

    fun getAllMedicalDataByPatientId(patientId: String): List<MedicalDataResponse> {
        val medicalDataList = medicalDataRepository.findByPatientId(patientId)
        if (medicalDataList.isEmpty()) {
            throw NoSuchElementException("No medical data found for patient with ID: $patientId")
        }
        return medicalDataList.map { MedicalDataMapper.toResponse(it) }
    }

    fun getMedicalDataById(id: String): MedicalDataResponse {
        val medicalData = medicalDataRepository.findById(UUID.fromString(id))
            .orElseThrow { NoSuchElementException("Medical data not found with ID: $id") }
        return MedicalDataMapper.toResponse(medicalData)
    }
}