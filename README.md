# Toucan Transaction Service

## Overview

This project implements a Customer Transaction REST API using Java 17, Spring Boot, Spring Data JPA, and H2 Database.

The application provides APIs to create transactions, retrieve transactions, update transaction status, and retrieve all transactions belonging to a customer.

## Technologies

- Java 17
- Spring Boot 3.5.5
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5
- MockMvc

## Transaction Fields

Every transaction contains:

- Transaction ID
- Customer ID
- Amount
- Currency
- Transaction Type
- Transaction Status

## APIs

### 1. Create Transaction

**POST** `/api/transactions`

Example request:

```json
{
  "transactionId": "TXN002",
  "customerId": "CUS002",
  "amount": 1000.00,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "status": "PENDING"
}

A new transaction must start with `PENDING` status.

### 2. Get Transaction

**GET** `/api/transactions/{transactionId}`

Example:

`GET /api/transactions/TXN001`

Returns the transaction when it exists.

Returns `404 Not Found` when the transaction does not exist.

### 3. Update Transaction Status

**PATCH** `/api/transactions/{transactionId}/status?status=COMPLETED`

Example:

`PATCH /api/transactions/TXN001/status?status=COMPLETED`

Supported statuses:

- `PENDING`
- `COMPLETED`
- `FAILED`
- `CANCELLED`

### 4. Get Customer Transactions

**GET** `/api/transactions/customer/{customerId}`

Example:

`GET /api/transactions/customer/CUS001`

Returns all transactions belonging to the specified customer.

## Validation

The following validations are implemented:

- Transaction ID is required.
- Customer ID is required.
- Amount must be greater than zero.
- Currency is required.
- Transaction type is required.
- New transactions must start with PENDING status.
- Duplicate transaction IDs are rejected.
- A transaction that does not exist returns 404.

## Error Handling

The application handles common errors using a global exception handler.

- `400 Bad Request` - invalid request data
- `404 Not Found` - transaction does not exist
- `409 Conflict` - duplicate transaction ID

## Database

The application uses an in-memory H2 database.

JDBC URL:

`jdbc:h2:mem:transactions`

H2 Console:

`/h2-console`

## Testing

The project contains meaningful tests covering:

1. Creating a transaction
2. Getting a transaction
3. Updating transaction status
4. Getting customer transactions

The Spring application context is also tested.

Test result:

`Tests run: 5, Failures: 0, Errors: 0, Skipped: 0`

## How to Run

### Windows

Run the following command from the project directory:

```text
mvnw.cmd clean test
```

To start the application:

```text
mvnw.cmd spring-boot:run
```

## AI Usage Disclosure

AI tools were used for assistance with understanding requirements, debugging, code suggestions, and documentation. The final implementation was reviewed and tested by the candidate.