package com.healthapi.services

import com.healthapi.exceptions.EntityNotFoundException
import com.healthapi.exceptions.ResourceAlreadyExistsException
import com.healthapi.models.Patient
import com.healthapi.repositories.PatientRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PatientService(private val patientRepository: PatientRepository) {
    
    fun getAllPatients(): List<Patient> {
        return patientRepository.findAll()
    }
    
    fun getPatientById(id: String): Patient {
        return patientRepository.findById(id)
    }
    
    fun createPatient(patient: Patient): Patient {
        // Check if email is already used
        if (patientRepository.existsByEmail(patient.email)) {
            throw ResourceAlreadyExistsException("Patient with email ${patient.email} already exists")
        }
        
        // Create with updated timestamps
        val newPatient = patient.copy(
            createdAt = LocalDate.now(),
            updatedAt = LocalDate.now()
        )
        
        return patientRepository.save(newPatient)
    }
    
    fun updatePatient(id: String, patientDetails: Patient): Patient {
        val existingPatient = patientRepository.findById(id)
        
        // Check if email is already used by another patient
        patientRepository.findByEmail(patientDetails.email)?.let {
            if (it.id != id) {
                throw ResourceAlreadyExistsException("Email ${patientDetails.email} is already in use by another patient")
            }
        }
        
        // Update the patient with existing id and updated timestamp
        val updatedPatient = patientDetails.copy(
            id = existingPatient.id,
            createdAt = existingPatient.createdAt,
            updatedAt = LocalDate.now()
        )
        
        return patientRepository.save(updatedPatient)
    }
    
    fun deletePatient(id: String): Boolean {
        // Verify patient exists
        patientRepository.findById(id)
        return patientRepository.deleteById(id)
    }
    
    fun searchPatientsByName(name: String): List<Patient> {
        if (name.isBlank()) {
            return emptyList()
        }
        return patientRepository.findByName(name)
    }
    
    fun getPatientsByBirthDateRange(startDate: LocalDate, endDate: LocalDate): List<Patient> {
        return patientRepository.findAll().filter {
            it.dateOfBirth.isAfter(startDate.minusDays(1)) && 
            it.dateOfBirth.isBefore(endDate.plusDays(1))
        }
    }
    
    fun getPatientStatistics(): Map<String, Any> {
        val patients = patientRepository.findAll()
        val totalPatients = patients.size
        
        val genderDistribution = patients.groupBy { it.gender }
            .mapValues { it.value.size.toDouble() / totalPatients }
        
        val ageGroups = patients.groupBy { calculateAgeGroup(it.dateOfBirth) }
            .mapValues { it.value.size }
        
        return mapOf(
            "totalPatients" to totalPatients,
            "genderDistribution" to genderDistribution,
            "ageGroups" to ageGroups
        )
    }
    
    private fun calculateAgeGroup(dateOfBirth: LocalDate): String {
        val age = LocalDate.now().year - dateOfBirth.year
        return when {
            age < 18 -> "Under 18"
            age < 30 -> "18-29"
            age < 45 -> "30-44"
            age < 60 -> "45-59"
            else -> "60+"
        }
    }
}
