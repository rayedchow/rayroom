package com.healthapi.repositories

import com.healthapi.config.FileStorageProperties
import com.healthapi.models.TestResult
import com.healthapi.models.TestStatus
import com.healthapi.models.TestType
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TestResultRepository(fileStorageProperties: FileStorageProperties) : 
    JsonFileRepository<TestResult, String>(fileStorageProperties, TestResult::class.java, "test_results") {
    
    override fun getId(entity: TestResult): String = entity.id
    
    fun findByPatientId(patientId: String): List<TestResult> {
        return findAll().filter { it.patientId == patientId }
    }
    
    fun findByTestType(testType: TestType): List<TestResult> {
        return findAll().filter { it.testType == testType }
    }
    
    fun findByStatus(status: TestStatus): List<TestResult> {
        return findAll().filter { it.status == status }
    }
    
    fun findByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<TestResult> {
        return findAll().filter { 
            it.testDateTime.isAfter(startDate) && it.testDateTime.isBefore(endDate) 
        }
    }
    
    fun findByPatientIdAndTestType(patientId: String, testType: TestType): List<TestResult> {
        return findAll().filter { 
            it.patientId == patientId && it.testType == testType 
        }
    }
    
    fun findLatestByPatientId(patientId: String, limit: Int = 5): List<TestResult> {
        return findByPatientId(patientId)
            .sortedByDescending { it.testDateTime }
            .take(limit)
    }
}
