package com.example.medicaldataservice.error

enum class ErrorCode(val code: Int, val identifier: String, val description: String) {
    VALIDATION_FAILED(4000, "ERR_VALIDATION_FAILED", "Validation failed"),
    INVALID_ARGUMENT(4001, "ERR_INVALID_ARGUMENT", "Invalid argument provided"),
    INVALID_JSON(4002, "ERR_INVALID_JSON", "Invalid JSON format"),

    RESOURCE_NOT_FOUND(4040, "ERR_NOT_FOUND", "The requested resource was not found"),

    METHOD_NOT_ALLOWED(4050, "ERR_METHOD_NOT_ALLOWED", "The method is not allowed"),

    ACCESS_DENIED(4030, "ERR_ACCESS_DENIED", "Access to the resource is denied"),
    INTERNAL_SERVER_ERROR(5000, "ERR_INTERNAL_SERVER_ERROR", "An unexpected error occurred")
}