package com.healthapi.services

import com.healthapi.exceptions.EntityNotFoundException
import com.healthapi.models.TestResult
import com.healthapi.models.TestStatus
import com.healthapi.models.TestType
import com.healthapi.repositories.PatientRepository
import com.healthapi.repositories.TestResultRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TestResultService(
    private val testResultRepository: TestResultRepository,
    private val patientRepository: PatientRepository
) {
    
    fun getAllTestResults(): List<TestResult> {
        return testResultRepository.findAll()
    }
    
    fun getTestResultById(id: String): TestResult {
        return testResultRepository.findById(id)
    }
    
    fun createTestResult(testResult: TestResult): TestResult {
        // Verify patient exists
        patientRepository.findById(testResult.patientId)
        
        // Create with updated timestamps
        val newTestResult = testResult.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        return testResultRepository.save(newTestResult)
    }
    
    fun updateTestResult(id: String, testResultDetails: TestResult): TestResult {
        val existingTestResult = testResultRepository.findById(id)
        
        // Verify patient exists if patient ID has changed
        if (existingTestResult.patientId != testResultDetails.patientId) {
            patientRepository.findById(testResultDetails.patientId)
        }
        
        // Update the test result with existing id and updated timestamp
        val updatedTestResult = testResultDetails.copy(
            id = existingTestResult.id,
            createdAt = existingTestResult.createdAt,
            updatedAt = LocalDateTime.now()
        )
        
        return testResultRepository.save(updatedTestResult)
    }
    
    fun deleteTestResult(id: String): Boolean {
        // Verify test result exists
        testResultRepository.findById(id)
        return testResultRepository.deleteById(id)
    }
    
    fun getTestResultsByPatientId(patientId: String): List<TestResult> {
        // Verify patient exists
        patientRepository.findById(patientId)
        return testResultRepository.findByPatientId(patientId)
    }
    
    fun getLatestTestResultsByPatientId(patientId: String, limit: Int = 5): List<TestResult> {
        // Verify patient exists
        patientRepository.findById(patientId)
        return testResultRepository.findLatestByPatientId(patientId, limit)
    }
    
    fun getTestResultsByType(testType: TestType): List<TestResult> {
        return testResultRepository.findByTestType(testType)
    }
    
    fun getTestResultsByStatus(status: TestStatus): List<TestResult> {
        return testResultRepository.findByStatus(status)
    }
    
    fun getTestResultsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<TestResult> {
        return testResultRepository.findByDateRange(startDate, endDate)
    }
    
    fun getTestResultStatistics(): Map<String, Any> {
        val testResults = testResultRepository.findAll()
        val totalTests = testResults.size
        
        val typeDistribution = testResults.groupBy { it.testType }
            .mapValues { it.value.size }
        
        val statusDistribution = testResults.groupBy { it.status }
            .mapValues { it.value.size }
        
        val testsPerDay = testResults
            .groupBy { it.testDateTime.toLocalDate() }
            .mapValues { it.value.size }
            .toSortedMap()
            .takeIf { it.isNotEmpty() }
            ?.let { map ->
                val lastTwoWeeks = (0..13).map { 
                    LocalDateTime.now().toLocalDate().minusDays(it.toLong()) 
                }.reversed()
                
                lastTwoWeeks.associate { date ->
                    date.toString() to (map[date] ?: 0)
                }
            } ?: emptyMap()
        
        return mapOf(
            "totalTests" to totalTests,
            "typeDistribution" to typeDistribution,
            "statusDistribution" to statusDistribution,
            "testsPerDay" to testsPerDay
        )
    }
    
    fun updateTestResultStatus(id: String, status: TestStatus): TestResult {
        val testResult = testResultRepository.findById(id)
        val updatedTestResult = testResult.copy(
            status = status,
            updatedAt = LocalDateTime.now()
        )
        return testResultRepository.save(updatedTestResult)
    }
    
    fun getTestResultsByPatientIdAndType(patientId: String, testType: TestType): List<TestResult> {
        // Verify patient exists
        patientRepository.findById(patientId)
        return testResultRepository.findByPatientIdAndTestType(patientId, testType)
    }
}
