# Toucan Customer Transaction Service

A Java 17 + Spring Boot REST API developed to manage customer transactions. The application supports transaction creation, retrieval, status updates, and customer-based transaction lookup using Spring Data JPA and H2 Database.

## Project Overview

The service provides four main operations:

- **Create** a customer transaction
- **Retrieve** a transaction using its unique Transaction ID
- **Update** the status of an existing transaction
- **Retrieve** all transactions belonging to a customer

The project follows a layered architecture:

**Controller → Service → Repository → Database**

The application was developed and executed using **Eclipse IDE**, with REST APIs manually verified using **Postman**.

## Objectives

The main objectives of this project are to demonstrate:

- REST API development
- Java and Spring Boot fundamentals
- Object-oriented programming
- Database interaction using JPA
- Input validation
- Business-rule validation
- Exception handling
- Automated testing
- Manual API testing

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Application development |
| Spring Boot 3.5.5 | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Database operations |
| Hibernate | Object-relational mapping |
| H2 Database | In-memory database |
| Maven | Build and dependency management |
| Jakarta Bean Validation | Input validation |
| JUnit 5 | Automated testing |
| MockMvc | REST API testing |
| Eclipse | Development environment |
| Postman | Manual API testing |

## Architecture and Layer Responsibilities

The application follows a simple layered architecture:

**Client → TransactionController → TransactionService → TransactionRepository → H2 Database**

- **Controller Layer:** Receives HTTP requests, validates request data, calls the service layer, and returns appropriate HTTP responses.
- **Service Layer:** Contains business logic, checks duplicate Transaction IDs, ensures a newly created transaction starts with `PENDING`, and handles transaction retrieval and status updates.
- **Repository Layer:** Uses Spring Data JPA to perform database operations and retrieve transactions based on Customer ID.
- **Entity Layer:** Represents transaction data, defines validation rules, and contains transaction status and transaction type enums.
- **Exception Layer:** Contains custom exceptions and provides centralized error handling through `GlobalExceptionHandler`.

## Project Structure

    src/main/java/com/example/transactionstarter
    │
    ├── controller
    │   └── TransactionController.java
    │
    ├── service
    │   └── TransactionService.java
    │
    ├── repository
    │   └── TransactionRepository.java
    │
    ├── entity
    │   ├── Transaction.java
    │   ├── TransactionStatus.java
    │   └── TransactionType.java
    │
    ├── exception
    │   ├── DuplicateTransactionException.java
    │   ├── ResourceNotFoundException.java
    │   └── GlobalExceptionHandler.java
    │
    └── TransactionStarterApplication.java

    src/main/resources
    └── application.yml

    src/test/java
    ├── TransactionControllerTest.java
    └── TransactionStarterApplicationTests.java

    pom.xml
    README.md

## Transaction Model

The `Transaction` entity contains the following fields:

| Field | Description | Validation |
|---|---|---|
| `transactionId` | Unique transaction identifier | Required, non-blank and unique |
| `customerId` | Customer identifier | Required and non-blank |
| `amount` | Transaction amount | Required and greater than 0 |
| `currency` | Three-letter currency code | Required and exactly 3 uppercase letters |
| `transactionType` | Type of transaction | Must be a supported enum value |
| `status` | Current transaction status | Must be a supported enum value |

### Transaction Types

The application supports:

- `PAYMENT`
- `REFUND`
- `TRANSFER`
- `WITHDRAWAL`

### Transaction Statuses

The application supports:

- `PENDING`
- `COMPLETED`
- `FAILED`
- `CANCELLED`

A newly created transaction must have the initial status `PENDING`.

Duplicate Transaction IDs are rejected before the transaction is saved.

## REST API Endpoints

| Operation | HTTP Method | Endpoint | Success Response |
|---|---|---|---|
| Create transaction | `POST` | `/api/transactions` | `201 Created` |
| Get transaction | `GET` | `/api/transactions/{transactionId}` | `200 OK` |
| Update transaction status | `PATCH` | `/api/transactions/{transactionId}/status?status={status}` | `200 OK` |
| Get customer transactions | `GET` | `/api/transactions/customer/{customerId}` | `200 OK` |

### Create Transaction

**Request:**

    POST /api/transactions
    Content-Type: application/json

**Request Body:**

    {
      "transactionId": "TXN001",
      "customerId": "CUS001",
      "amount": 500.00,
      "currency": "INR",
      "transactionType": "PAYMENT",
      "status": "PENDING"
    }

**Expected Response:** `201 Created`

The transaction is validated and stored in the H2 database.

### Get Transaction

**Request:**

    GET /api/transactions/TXN001

**Expected Response:** `200 OK`

The API returns the transaction associated with the specified Transaction ID.

### Update Transaction Status

**Request:**

    PATCH /api/transactions/TXN001/status?status=COMPLETED

**Expected Response:** `200 OK`

The service finds the transaction, updates its status, saves the updated transaction, and returns the updated record.

Supported status values are:

- `PENDING`
- `COMPLETED`
- `FAILED`
- `CANCELLED`

### Get Customer Transactions

**Request:**

    GET /api/transactions/customer/CUS001

**Expected Response:** `200 OK`

The API returns all transactions associated with the specified Customer ID.

## Validation

The application uses **Jakarta Bean Validation** for request validation.

### Transaction ID

- Must not be blank.
- Must be unique.

### Customer ID

- Must not be blank.

### Amount

- Must not be null.
- Must be greater than `0`.

### Currency

- Must not be blank.
- Must contain exactly three uppercase letters.

Examples: `INR`, `USD`, `EUR`

### Transaction Type

The value must match one of the supported transaction types:

`PAYMENT`, `REFUND`, `TRANSFER`, `WITHDRAWAL`

### Transaction Status

The value must match one of the supported statuses:

`PENDING`, `COMPLETED`, `FAILED`, `CANCELLED`

### Business Validation

In addition to field validation, the service layer performs business validation:

- A new transaction must start with `PENDING`.
- A duplicate Transaction ID is not allowed.
- A transaction must exist before its status can be updated.
- A transaction must exist before it can be retrieved.

## Exception Handling

The application uses centralized exception handling through `GlobalExceptionHandler.java`.

The following errors are handled:

| Situation | HTTP Status |
|---|---|
| Invalid request data | `400 Bad Request` |
| Invalid business input | `400 Bad Request` |
| Transaction not found | `404 Not Found` |
| Duplicate Transaction ID | `409 Conflict` |

### Custom Exceptions

- `DuplicateTransactionException`
- `ResourceNotFoundException`

This provides consistent and meaningful error responses to API clients.

## Database

The application uses an **H2 in-memory database**.

**Database URL:**

    jdbc:h2:mem:transactions

The database configuration is available in:

    src/main/resources/application.yml

The application uses:

    spring.jpa.hibernate.ddl-auto: create-drop

This means the database schema is created when the application starts and removed when the application stops.

### H2 Console

The H2 console is enabled and can be accessed at:

    http://localhost:8080/h2-console

The H2 database is used for simplicity during development and testing. Since it is an in-memory database, stored transaction data is temporary.

## Automated Testing

The project uses:

- JUnit 5
- Spring Boot Test
- MockMvc

The automated test suite covers the main application functionality.

### Test 1 — Create Transaction

Verifies that a valid transaction can be created successfully and returns `201 Created`.

### Test 2 — Get Transaction

Verifies that an existing transaction can be retrieved successfully and returns `200 OK`.

### Test 3 — Update Transaction Status

Verifies that the status of an existing transaction can be updated successfully.

Example: `PENDING → COMPLETED`

### Test 4 — Get Customer Transactions

Verifies that all transactions belonging to a customer can be retrieved successfully.

### Test 5 — Application Context

Verifies that the Spring Boot application context loads successfully.

### Final Test Result

    Tests run: 5
    Failures: 0
    Errors: 0
    Skipped: 0

    BUILD SUCCESS

## Manual API Testing

All four REST APIs were manually tested using **Postman**:

- ✓ Create Transaction
- ✓ Get Transaction
- ✓ Update Transaction Status
- ✓ Get Customer Transactions

The H2 database was also checked to confirm that transaction records were stored and updated correctly.

## How to Run the Project

### Prerequisites

Make sure the following are installed:

- Java 17
- Maven
- Eclipse IDE
- Postman

### Steps

**Step 1:** Import the project into Eclipse as a Maven project.

**Step 2:** Make sure the project uses Java 17.

**Step 3:** Allow Maven to download all required dependencies.

**Step 4:** Run `TransactionStarterApplication.java`.

**Step 5:** The application starts on:

    http://localhost:8080

**Step 6:** Open Postman and test the REST API endpoints.

## How to Run Tests

In Eclipse:

    Right-click Project
            ↓
        Run As
            ↓
        Maven test

The Maven test command executes the complete automated test suite.

## Challenge Requirements Completed

- ✓ Create transaction
- ✓ Get transaction
- ✓ Update transaction status
- ✓ Get customer transactions
- ✓ Input validation
- ✓ Business validation
- ✓ Duplicate Transaction ID protection
- ✓ Exception handling
- ✓ H2 database integration
- ✓ Spring Data JPA
- ✓ Automated testing
- ✓ Postman API testing
- ✓ AI assistance disclosure

## AI Assistance Disclosure

AI tools, including **ChatGPT**, were used as an assistance resource for understanding the assignment requirements, Spring Boot concepts, debugging, validation, testing guidance, and documentation.

The suggestions were reviewed and adapted to the actual project implementation. The final application was executed and verified by me through automated tests and manual API testing using Postman.

---

## Conclusion

The Customer Transaction Service successfully implements a complete REST-based transaction management system using Java 17 and Spring Boot. It provides reliable transaction creation, retrieval, status updates, and customer-wise transaction search. The application also includes input validation, business validation, exception handling, database integration, and automated testing. The implemented APIs were successfully tested using Postman, and the complete test suite executed successfully. This project demonstrates a practical understanding of Spring Boot, REST APIs, JPA, H2 Database, validation, and testing.
