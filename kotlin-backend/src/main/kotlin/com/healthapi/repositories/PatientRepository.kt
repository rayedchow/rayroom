package com.healthapi.repositories

import com.healthapi.config.FileStorageProperties
import com.healthapi.models.Patient
import org.springframework.stereotype.Repository

@Repository
class PatientRepository(fileStorageProperties: FileStorageProperties) : 
    JsonFileRepository<Patient, String>(fileStorageProperties, Patient::class.java, "patients") {
    
    override fun getId(entity: Patient): String = entity.id
    
    fun findByName(name: String): List<Patient> {
        val lowercaseName = name.lowercase()
        return findAll().filter { 
            it.firstName.lowercase().contains(lowercaseName) || 
            it.lastName.lowercase().contains(lowercaseName) 
        }
    }
    
    fun findByEmail(email: String): Patient? {
        return findAll().find { it.email.equals(email, ignoreCase = true) }
    }
    
    fun existsByEmail(email: String): Boolean {
        return findByEmail(email) != null
    }
}
