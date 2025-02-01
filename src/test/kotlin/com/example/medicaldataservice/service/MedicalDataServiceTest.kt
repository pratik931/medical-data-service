package com.example.medicaldataservice.service

import com.example.medicaldataservice.domain.BloodPressure
import com.example.medicaldataservice.domain.MedicalData
import com.example.medicaldataservice.dto.BloodPressureRequest
import com.example.medicaldataservice.dto.MedicalDataRequest
import com.example.medicaldataservice.repository.MedicalDataRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

class MedicalDataServiceTest {

    private val medicalDataRepository: MedicalDataRepository = mock()
    private val medicalDataService = MedicalDataService(medicalDataRepository)

    @Test
    fun `saveMedicalData() should save and return created response`() {
        val request = createSampleRequest()
        val medicalData = createSampleMedicalData()
        whenever(medicalDataRepository.save(any<MedicalData>())).thenReturn(medicalData)

        val response = medicalDataService.saveMedicalData(request)

        assertEquals(medicalData.id.toString(), response.id)
        verify(medicalDataRepository).save(any())
    }


    @Test
    fun `getAllMedicalDataByPatientId() should return list of medical data responses`() {
        val patientId = "patient-123"
        val medicalDataList = listOf(createSampleMedicalData())
        whenever(medicalDataRepository.findByPatientId(patientId)).thenReturn(medicalDataList)

        val result = medicalDataService.getAllMedicalDataByPatientId(patientId)

        assertEquals(1, result.size)
        assertEquals(patientId, result[0].patientId)
        verify(medicalDataRepository).findByPatientId(patientId)
    }

    @Test
    fun `getAllMedicalDataByPatientId() should throw exception if no data is found`() {
        val patientId = "non-existent-patient"
        whenever(medicalDataRepository.findByPatientId(patientId)).thenReturn(emptyList())

        val exception = assertThrows<NoSuchElementException> {
            medicalDataService.getAllMedicalDataByPatientId(patientId)
        }
        assertEquals("No medical data found for patient with ID: $patientId", exception.message)
        verify(medicalDataRepository).findByPatientId(patientId)
    }

    @Test
    fun `getMedicalDataById() should return medical data response`() {
        val medicalData = createSampleMedicalData()
        val id = medicalData.id.toString()
        whenever(medicalDataRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(medicalData))

        val result = medicalDataService.getMedicalDataById(id)

        assertEquals(id, result.id)
        verify(medicalDataRepository).findById(UUID.fromString(id))
    }

    @Test
    fun `getMedicalDataById() should throw exception if no data is found`() {
        val id = UUID.randomUUID().toString()
        whenever(medicalDataRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> {
            medicalDataService.getMedicalDataById(id)
        }
        assertEquals("Medical data not found with ID: $id", exception.message)
        verify(medicalDataRepository).findById(UUID.fromString(id))
    }

    private fun createSampleMedicalData(): MedicalData {
        return MedicalData(
            id = UUID.randomUUID(),
            patientId = "patient-123",
            bloodPressure = BloodPressure(120, 80),
            heartbeatRate = 75,
            createdAt = LocalDateTime.now()
        )
    }

    private fun createSampleRequest(): MedicalDataRequest {
        return MedicalDataRequest(
            patientId = "patient-123",
            bloodPressure = BloodPressureRequest(120, 80),
            heartbeatRate = 75
        )
    }
}