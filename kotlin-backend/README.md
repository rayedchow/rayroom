# Health API - Kotlin Backend REST API

A comprehensive Kotlin-based backend REST API for managing patients and medical test results using Spring Boot and JSON file-based storage.

## Project Overview

This API provides a complete solution for healthcare management systems, allowing:
- Patient management (CRUD operations)
- Test result management (blood tests, urine tests, imaging, etc.)
- Search and filter capabilities for patients and test results
- Data persistence using JSON files

## Technology Stack

- **Language**: Kotlin 1.8.21
- **Framework**: Spring Boot 3.1.0
- **Build Tool**: Gradle 
- **Database**: JSON file-based storage
- **Documentation**: OpenAPI/Swagger

## Project Structure

```
kotlin-backend/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/
│   │   │       └── healthapi/
│   │   │           ├── controllers/     # REST API endpoints
│   │   │           ├── models/          # Data models
│   │   │           ├── repositories/    # JSON file data access
│   │   │           ├── services/        # Business logic
│   │   │           ├── config/          # Configuration classes
│   │   │           ├── exceptions/      # Custom exceptions
│   │   │           └── utils/           # Utility classes
│   │   └── resources/
│   │       ├── application.properties   # Application settings
│   │       └── data/                    # JSON data files 
│   └── test/                            # Unit and integration tests
├── build.gradle.kts                     # Dependency management
└── README.md                            # Documentation
```

## Key Features

### Patient Management
- Create, read, update, and delete patient records
- Search patients by name
- Filter patients by birth date range
- Get patient statistics (age distribution, gender, etc.)

### Test Result Management
- Record various types of medical tests (blood, urine, imaging, etc.)
- Associate test results with patients
- Track test status (ordered, in progress, completed, etc.)
- Filter test results by type, status, date range
- Get test statistics

### Data Storage
- JSON file-based storage with thread-safe access
- Automatic file creation and directory management
- GSON serialization with custom type adapters for dates

## API Endpoints

### Patient Endpoints
- `GET /api/patients` - Get all patients
- `GET /api/patients/{id}` - Get patient by ID
- `POST /api/patients` - Create a new patient
- `PUT /api/patients/{id}` - Update a patient
- `DELETE /api/patients/{id}` - Delete a patient
- `GET /api/patients/search?name={name}` - Search patients by name
- `GET /api/patients/birthdate-range?startDate={date}&endDate={date}` - Get patients by birth date range
- `GET /api/patients/statistics` - Get patient statistics

### Test Result Endpoints
- `GET /api/test-results` - Get all test results
- `GET /api/test-results/{id}` - Get test result by ID
- `POST /api/test-results` - Create a new test result
- `PUT /api/test-results/{id}` - Update a test result
- `DELETE /api/test-results/{id}` - Delete a test result
- `GET /api/test-results/patient/{patientId}` - Get test results for a patient
- `GET /api/test-results/patient/{patientId}/latest` - Get latest test results for a patient
- `GET /api/test-results/type/{testType}` - Get test results by type
- `GET /api/test-results/status/{status}` - Get test results by status
- `GET /api/test-results/date-range?startDate={date}&endDate={date}` - Get test results by date range
- `GET /api/test-results/statistics` - Get test result statistics
- `PUT /api/test-results/{id}/status?status={status}` - Update test result status

## Getting Started

### Prerequisites
- JDK 17 or later
- Gradle 7.6 or later

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```
   ./gradlew bootRun
   ```
4. The API will be available at http://localhost:8080
5. Swagger documentation: http://localhost:8080/swagger-ui.html

## Data Storage

The application uses JSON files to store data under `src/main/resources/data/`:
- `patients.json` - Patient records
- `test_results.json` - Test result records

## Development

### Adding a New Entity Type
1. Create a model class in `com.healthapi.models`
2. Create a repository class in `com.healthapi.repositories` extending `JsonFileRepository`
3. Create a service class in `com.healthapi.services`
4. Create a controller in `com.healthapi.controllers`

## Security Considerations

For production use, consider implementing:
- Authentication and authorization 
- Input validation
- HTTPS encryption
- Rate limiting
- Auditing and logging

## License

This project is licensed under the MIT License.
