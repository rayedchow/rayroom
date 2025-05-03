package com.healthapi.controllers

import com.healthapi.models.Patient
import com.healthapi.services.PatientService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/patients")
class PatientController(private val patientService: PatientService) {

    @GetMapping
    fun getAllPatients(): ResponseEntity<List<Patient>> {
        return ResponseEntity.ok(patientService.getAllPatients())
    }

    @GetMapping("/{id}")
    fun getPatientById(@PathVariable id: String): ResponseEntity<Patient> {
        return ResponseEntity.ok(patientService.getPatientById(id))
    }

    @PostMapping
    fun createPatient(@RequestBody patient: Patient): ResponseEntity<Patient> {
        val createdPatient = patientService.createPatient(patient)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient)
    }

    @PutMapping("/{id}")
    fun updatePatient(
        @PathVariable id: String,
        @RequestBody patientDetails: Patient
    ): ResponseEntity<Patient> {
        return ResponseEntity.ok(patientService.updatePatient(id, patientDetails))
    }

    @DeleteMapping("/{id}")
    fun deletePatient(@PathVariable id: String): ResponseEntity<Void> {
        patientService.deletePatient(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search")
    fun searchPatients(@RequestParam name: String): ResponseEntity<List<Patient>> {
        return ResponseEntity.ok(patientService.searchPatientsByName(name))
    }

    @GetMapping("/birthdate-range")
    fun getPatientsByBirthDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<List<Patient>> {
        return ResponseEntity.ok(patientService.getPatientsByBirthDateRange(startDate, endDate))
    }

    @GetMapping("/statistics")
    fun getPatientStatistics(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(patientService.getPatientStatistics())
    }
}
