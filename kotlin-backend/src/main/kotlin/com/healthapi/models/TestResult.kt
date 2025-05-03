package com.healthapi.models

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class TestResult(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val testType: TestType,
    val testDateTime: LocalDateTime = LocalDateTime.now(),
    val performedBy: String,
    val resultValues: Map<String, Any>,
    val normalRanges: Map<String, Range<*>>? = null,
    val interpretation: String,
    val status: TestStatus,
    val notes: String? = null,
    val attachments: List<Attachment> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class TestType {
    BLOOD_TEST,
    URINE_TEST,
    IMAGING,
    PATHOLOGY,
    CARDIOLOGY,
    NEUROLOGICAL,
    GENETIC,
    OTHER
}

enum class TestStatus {
    ORDERED,
    SCHEDULED,
    COLLECTED,
    IN_PROGRESS,
    COMPLETED,
    REVIEWED,
    CANCELLED
}

data class Attachment(
    val id: String = UUID.randomUUID().toString(),
    val fileName: String,
    val fileType: String,
    val description: String? = null,
    val uploadedAt: LocalDateTime = LocalDateTime.now(),
    val filePath: String
)

data class Range<T : Comparable<T>>(
    val min: T,
    val max: T
) {
    fun isWithinRange(value: T): Boolean {
        return value >= min && value <= max
    }
}

// Specific test result types
data class BloodTestResult(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val testDate: LocalDate,
    val hemoglobin: Double,
    val whiteBloodCellCount: Double,
    val plateletCount: Double,
    val hematocrit: Double,
    val redBloodCellCount: Double,
    val meanCorpuscularVolume: Double,
    val meanCorpuscularHemoglobin: Double,
    val neutrophilsPercentage: Double,
    val lymphocytesPercentage: Double,
    val monocytesPercentage: Double,
    val eosinophilsPercentage: Double,
    val basophilsPercentage: Double,
    val glucose: Double,
    val cholesterol: Double,
    val triglycerides: Double,
    val hdlCholesterol: Double,
    val ldlCholesterol: Double,
    val sodium: Double,
    val potassium: Double,
    val chloride: Double,
    val calcium: Double,
    val magnesium: Double,
    val notes: String? = null
)

data class UrineTestResult(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val testDate: LocalDate,
    val color: String,
    val appearance: String,
    val specificGravity: Double,
    val pH: Double,
    val protein: String,
    val glucose: String,
    val ketones: String,
    val bilirubin: String,
    val blood: String,
    val urobilinogen: String,
    val nitrites: String,
    val leukocytes: String,
    val bacteria: String,
    val epithelialCells: String,
    val crystals: String,
    val casts: String,
    val notes: String? = null
)

data class ImagingResult(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val imagingType: ImagingType,
    val testDate: LocalDate,
    val bodyPart: String,
    val findings: String,
    val impression: String,
    val recommendations: String? = null,
    val imagePaths: List<String>,
    val notes: String? = null
)

enum class ImagingType {
    X_RAY,
    CT_SCAN,
    MRI,
    ULTRASOUND,
    PET_SCAN,
    MAMMOGRAM,
    BONE_DENSITY,
    OTHER
}
