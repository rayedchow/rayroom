package com.healthapi.models

import java.time.LocalDate
import java.util.UUID

data class Patient(
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val gender: Gender,
    val contactNumber: String,
    val email: String,
    val address: Address,
    val medicalHistory: List<MedicalHistoryItem> = emptyList(),
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String
)

data class MedicalHistoryItem(
    val condition: String,
    val diagnosedDate: LocalDate,
    val notes: String,
    val medication: List<Medication> = emptyList()
)

data class Medication(
    val name: String,
    val dosage: String,
    val frequency: String,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

enum class Gender {
    MALE, FEMALE, OTHER
}
