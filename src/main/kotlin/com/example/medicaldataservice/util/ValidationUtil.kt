package com.example.medicaldataservice.util

import com.example.medicaldataservice.dto.BloodPressureRequest
import com.example.medicaldataservice.dto.MedicalDataRequest

object ValidationUtil {
    fun validate(medicalDataRequest: MedicalDataRequest) {
        validateBloodPressure(medicalDataRequest.bloodPressure)
    }

    private fun validateBloodPressure(bloodPressure: BloodPressureRequest?) {
        if (bloodPressure != null) {
            if (bothBloodPressureReadingsNotProvided(bloodPressure)) {
                throw IllegalArgumentException("Both systolic and diastolic pressures must be provided together")
            }

            if (isSystolicLessThanDiastolic(bloodPressure.systolic, bloodPressure.diastolic)) {
                throw IllegalArgumentException("Systolic pressure (${bloodPressure.systolic}) " +
                        "must be greater than diastolic pressure (${bloodPressure.diastolic})")
            }
        }
    }

    private fun bothBloodPressureReadingsNotProvided(bloodPressure: BloodPressureRequest) =
        (bloodPressure.systolic == null) != (bloodPressure.diastolic == null)

    private fun isSystolicLessThanDiastolic(systolic: Int?, diastolic: Int?) =
        systolic != null && diastolic != null && systolic <= diastolic
}