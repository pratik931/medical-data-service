package com.example.medicaldataservice.exception

import com.example.medicaldataservice.dto.ErrorResponse
import com.example.medicaldataservice.error.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * Handle validation errors (e.g., @Valid annotations)
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        val errorCode = ErrorCode.VALIDATION_FAILED
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = errorCode.code,
            identifier = errorCode.identifier,
            message = errors,
            path = request.requestURI
        )
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Handle cases where data is not found
     */
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundException(
        ex: NoSuchElementException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.RESOURCE_NOT_FOUND
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            code = errorCode.code,
            identifier = errorCode.identifier,
            message = ex.message ?: "Resource not found",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * Handle cases where invalid arguments were provided
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_ARGUMENT
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = errorCode.code,
            identifier = errorCode.identifier,
            message = ex.message ?: "Invalid request parameters",
            path = request.requestURI
        )
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Handle unsupported HTTP methods
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupportedException(
        ex: HttpRequestMethodNotSupportedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.METHOD_NOT_ALLOWED
        val errorResponse = ErrorResponse(
            status = HttpStatus.METHOD_NOT_ALLOWED.value(),
            error = HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase,
            code = errorCode.code,
            identifier = errorCode.identifier,
            message = "The ${ex.method} method is not supported for this endpoint.",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse)
    }

    /**
     * Handle Invalid JSON request body
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidJsonException(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_JSON
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = errorCode.code,
            identifier = errorCode.identifier,
            message = "Malformed JSON request. Please ensure the JSON is correctly formatted.",
            path = request.requestURI
        )
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Handle Access Denied exceptions
     */
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.ACCESS_DENIED
        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            error = HttpStatus.FORBIDDEN.reasonPhrase,
            code = errorCode.code,
            identifier = errorCode.identifier,
            message = "You do not have permission to access this resource.",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    /**
     * Handle any unexpected exceptions
     */
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        logger.error("Unexpected error occurred at ${request.requestURI}: ${ex.message}", ex)

        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            code = errorCode.code,
            identifier = errorCode.identifier,
            message = "An unexpected error occurred. Please contact support.",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}