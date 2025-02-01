package com.example.medicaldataservice.controller

import com.example.medicaldataservice.dto.MedicalDataCreateResponse
import com.example.medicaldataservice.dto.MedicalDataRequest
import com.example.medicaldataservice.dto.MedicalDataResponse
import com.example.medicaldataservice.service.MedicalDataService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/medical-data")
class MedicalDataController(
    private val medicalDataService: MedicalDataService
) {
    private val logger = LoggerFactory.getLogger(MedicalDataController::class.java)

    @Operation(summary = "Create a new medical data record for the patient")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Medical data record created successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = MedicalDataResponse::class))]),
            ApiResponse(responseCode = "400", description = "Invalid request data", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    @PostMapping(
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun createMedicalData(
        @Valid @RequestBody request: MedicalDataRequest
    ): ResponseEntity<MedicalDataCreateResponse> {
        logger.debug("Received request to create medical data for patientId={}", request.patientId)
        val response = medicalDataService.saveMedicalData(request)
        logger.debug("Successfully created medical data with id={}", response.id)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "Get a medical data record by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Medical data found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = MedicalDataResponse::class))]),
            ApiResponse(responseCode = "404", description = "Medical data not found", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    @GetMapping(
        path = ["/{id}"],
        produces = ["application/json"] )
    fun getMedicalDataById(
        @PathVariable id: String
    ): ResponseEntity<MedicalDataResponse> {
        logger.debug("Received request to get medical data by ID={}", id)
        val response = medicalDataService.getMedicalDataById(id)
        logger.debug("Found medical data with ID={}", id)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Get all medical data records for a patient")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Medical data found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = MedicalDataResponse::class))]),
            ApiResponse(responseCode = "404", description = "No medical data found for the patient", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    @GetMapping(
        path = ["/patient/{patientId}"],
        produces = ["application/json"])
    fun getAllMedicalDataForPatient(
        @PathVariable patientId: String
    ): ResponseEntity<List<MedicalDataResponse>> {
        logger.debug("Received request to get all medical data for patientId={}", patientId)
        val responses = medicalDataService.getAllMedicalDataByPatientId(patientId)
        logger.debug("Found {} medical data records for patientId={}", responses.size, patientId)
        return ResponseEntity.ok(responses)
    }
}