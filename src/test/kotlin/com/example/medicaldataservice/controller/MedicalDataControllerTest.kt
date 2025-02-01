package com.example.medicaldataservice.controller

import com.example.medicaldataservice.security.SecurityConfig
import com.example.medicaldataservice.dto.MedicalDataCreateResponse
import com.example.medicaldataservice.dto.MedicalDataResponse
import com.example.medicaldataservice.security.CustomUserDetailsService
import com.example.medicaldataservice.service.MedicalDataService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.*

private const val API_MEDICAL_DATA = "/api/v1/medical-data"

private const val API_MEDICAL_DATA_PATIENT = "/api/v1/medical-data/patient"

@WebMvcTest(MedicalDataController::class)
@Import(SecurityConfig::class)
class MedicalDataControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var medicalDataService: MedicalDataService

    @MockitoBean
    private lateinit var customUserDetailsService: CustomUserDetailsService

    @Test
    @WithMockUser(username = "user1", roles = ["USER"])
    fun `createMedicalData() should return 201 status and response body on success`() {
        val response = MedicalDataCreateResponse(id = UUID.randomUUID().toString())

        whenever(medicalDataService.saveMedicalData(any())).thenReturn(response)

        mockMvc.perform(
            post(API_MEDICAL_DATA)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "patientId": "patient-123",
                        "heartbeatRate": 75
                    }
                """.trimIndent()))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(response.id))

        verify(medicalDataService).saveMedicalData(any())
    }

    @Test
    @WithMockUser(username = "user1", roles = ["USER"])
    fun `createMedicalData() should return 400 status for invalid input`() {
        mockMvc.perform(
            post(API_MEDICAL_DATA)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "user1", roles = ["USER"])
    fun `getMedicalDataById() should return 200 status and response body on success`() {
        val id = UUID.randomUUID().toString()
        val response = createSampleResponse()

        whenever(medicalDataService.getMedicalDataById(id)).thenReturn(response)

        mockMvc.perform(
            get(
                "$API_MEDICAL_DATA/$id")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(response.id))
            .andExpect(jsonPath("$.patientId").value(response.patientId))

        verify(medicalDataService).getMedicalDataById(id)
    }

    @Test
    @WithMockUser(username = "user1", roles = ["USER"])
    fun `getMedicalDataById() should return 404 status if data is not found`() {
        val id = UUID.randomUUID().toString()
        whenever(medicalDataService.getMedicalDataById(id))
            .thenThrow(NoSuchElementException("Medical data not found with ID: $id"))

        mockMvc.perform(
            get("$API_MEDICAL_DATA/$id")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Medical data not found with ID: $id"))
    }

    @Test
    @WithMockUser(username = "user1", roles = ["USER"])
    fun `getAllMedicalDataForPatient() should return 200 status and response body on success`() {
        val patientId = "patient-123"
        val responses = listOf(createSampleResponse())

        whenever(medicalDataService.getAllMedicalDataByPatientId(patientId)).thenReturn(responses)

        mockMvc.perform(
            get("$API_MEDICAL_DATA_PATIENT/$patientId")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].patientId").value(patientId))

        verify(medicalDataService).getAllMedicalDataByPatientId(patientId)
    }

    @Test
    @WithMockUser(username = "user1", roles = ["USER"])
    fun `getAllMedicalDataForPatient() should return 404 status if no data is found`() {
        val patientId = "non-existent-patient"
        whenever(medicalDataService.getAllMedicalDataByPatientId(patientId))
            .thenThrow(NoSuchElementException("No medical data found for patient with ID: $patientId"))

        mockMvc.perform(
            get("$API_MEDICAL_DATA_PATIENT/$patientId")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("No medical data found for patient with ID: $patientId"))
    }

    @Test
    fun `createMedicalData() should return 401 status for unauthorized user`() {
        mockMvc.perform(
            post(API_MEDICAL_DATA)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "patientId": "patient-123",
                        "heartbeatRate": 75
                    }
                """.trimIndent()))
            .andExpect(status().isUnauthorized)
    }

    private fun createSampleResponse(): MedicalDataResponse {
        return MedicalDataResponse(
            id = UUID.randomUUID().toString(),
            patientId = "patient-123",
            bloodPressure = null,
            heartbeatRate = 75,
            createdAt = LocalDateTime.now()
        )
    }
}