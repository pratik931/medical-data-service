package com.example.medicaldataservice.util

import com.example.medicaldataservice.dto.BloodPressureRequest
import com.example.medicaldataservice.dto.MedicalDataRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ValidationUtilTest {

    @Test
    fun `validate() should not throw exception for valid request`() {
        val request = createValidMedicalDataRequest()
        assertDoesNotThrow {
            ValidationUtil.validate(request)
        }
    }

    @Test
    fun `validateBloodPressure() should not throw exception for null bloodPressure`() {
        val request = createValidMedicalDataRequest().copy(bloodPressure = null)
        assertDoesNotThrow {
            ValidationUtil.validate(request)
        }
    }

    @Test
    fun `validateBloodPressure() should not throw exception for valid systolic and diastolic values`() {
        val bloodPressure = createBloodPressureRequest(120, 80)
        val request = createValidMedicalDataRequest().copy(bloodPressure = bloodPressure)
        assertDoesNotThrow {
            ValidationUtil.validate(request)
        }
    }

    @Test
    fun `validate() should throw exception when only systolic is provided`() {
        val request = createValidMedicalDataRequest().copy(
            bloodPressure = createBloodPressureRequest(120, null)
        )

        val exception = assertThrows<IllegalArgumentException> {
            ValidationUtil.validate(request)
        }
        assertEquals("Both systolic and diastolic pressures must be provided together", exception.message)
    }

    @Test
    fun `validate() should throw exception when only diastolic is provided`() {
        val request = createValidMedicalDataRequest().copy(
            bloodPressure = createBloodPressureRequest(null, 80)
        )

        val exception = assertThrows<IllegalArgumentException> {
            ValidationUtil.validate(request)
        }
        assertEquals("Both systolic and diastolic pressures must be provided together", exception.message)
    }

    @Test
    fun `validate() should throw exception when systolic is less than diastolic`() {
        val request = createValidMedicalDataRequest().copy(
            bloodPressure = createBloodPressureRequest(80, 120)
        )

        val exception = assertThrows<IllegalArgumentException> {
            ValidationUtil.validate(request)
        }
        assertEquals(
            "Systolic pressure (80) must be greater than diastolic pressure (120)",
            exception.message
        )
    }

    private fun createValidMedicalDataRequest(): MedicalDataRequest {
        return MedicalDataRequest(
            patientId = "12345",
            bloodPressure = BloodPressureRequest(120, 80),
            heartbeatRate = 75
        )
    }

    private fun createBloodPressureRequest(systolic: Int?, diastolic: Int?): BloodPressureRequest {
        return BloodPressureRequest(systolic, diastolic)
    }
}