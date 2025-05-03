package com.healthapi.controllers

import com.healthapi.models.TestResult
import com.healthapi.models.TestStatus
import com.healthapi.models.TestType
import com.healthapi.services.TestResultService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/test-results")
class TestResultController(private val testResultService: TestResultService) {

    @GetMapping
    fun getAllTestResults(): ResponseEntity<List<TestResult>> {
        return ResponseEntity.ok(testResultService.getAllTestResults())
    }

    @GetMapping("/{id}")
    fun getTestResultById(@PathVariable id: String): ResponseEntity<TestResult> {
        return ResponseEntity.ok(testResultService.getTestResultById(id))
    }

    @PostMapping
    fun createTestResult(@RequestBody testResult: TestResult): ResponseEntity<TestResult> {
        val createdTestResult = testResultService.createTestResult(testResult)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestResult)
    }

    @PutMapping("/{id}")
    fun updateTestResult(
        @PathVariable id: String,
        @RequestBody testResultDetails: TestResult
    ): ResponseEntity<TestResult> {
        return ResponseEntity.ok(testResultService.updateTestResult(id, testResultDetails))
    }

    @DeleteMapping("/{id}")
    fun deleteTestResult(@PathVariable id: String): ResponseEntity<Void> {
        testResultService.deleteTestResult(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/patient/{patientId}")
    fun getTestResultsByPatientId(@PathVariable patientId: String): ResponseEntity<List<TestResult>> {
        return ResponseEntity.ok(testResultService.getTestResultsByPatientId(patientId))
    }

    @GetMapping("/patient/{patientId}/latest")
    fun getLatestTestResultsByPatientId(
        @PathVariable patientId: String,
        @RequestParam(defaultValue = "5") limit: Int
    ): ResponseEntity<List<TestResult>> {
        return ResponseEntity.ok(testResultService.getLatestTestResultsByPatientId(patientId, limit))
    }

    @GetMapping("/type/{testType}")
    fun getTestResultsByType(@PathVariable testType: TestType): ResponseEntity<List<TestResult>> {
        return ResponseEntity.ok(testResultService.getTestResultsByType(testType))
    }

    @GetMapping("/status/{status}")
    fun getTestResultsByStatus(@PathVariable status: TestStatus): ResponseEntity<List<TestResult>> {
        return ResponseEntity.ok(testResultService.getTestResultsByStatus(status))
    }

    @GetMapping("/date-range")
    fun getTestResultsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<List<TestResult>> {
        return ResponseEntity.ok(testResultService.getTestResultsByDateRange(startDate, endDate))
    }

    @GetMapping("/statistics")
    fun getTestResultStatistics(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(testResultService.getTestResultStatistics())
    }

    @PutMapping("/{id}/status")
    fun updateTestResultStatus(
        @PathVariable id: String,
        @RequestParam status: TestStatus
    ): ResponseEntity<TestResult> {
        return ResponseEntity.ok(testResultService.updateTestResultStatus(id, status))
    }

    @GetMapping("/patient/{patientId}/type/{testType}")
    fun getTestResultsByPatientIdAndType(
        @PathVariable patientId: String,
        @PathVariable testType: TestType
    ): ResponseEntity<List<TestResult>> {
        return ResponseEntity.ok(testResultService.getTestResultsByPatientIdAndType(patientId, testType))
    }
}
