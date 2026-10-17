# Employee Management System

A robust, layered RESTful API built with Java and Spring Boot for managing employee records, integrating relational database persistence and automated test suites.

## 🚀 Tech Stack
* **Language:** Java (Java 17 / 21)
* **Framework:** Spring Boot 3
* **Data & Persistence:** Spring Data JPA, Hibernate, MySQL
* **Validation:** Spring Boot Starter Validation (`@Valid`, `@NotNull`)
* **Testing:** JUnit 5, Mockito, Spring Boot Test
* **Build Tool:** Apache Maven

## ✨ Key Features
* Full CRUD endpoints for managing employee records.
* Layered Architecture: **Controller** -> **Service** -> **Repository (DAO)** -> **Entity**.
* Global Exception Handling using `@RestControllerAdvice`.
* Request body payload validation using `@Valid`.
* Database initialization check (seeds/verifies employee records on startup).

## 🧪 Testing & Verification
This project includes automated unit and integration tests. To build the project and execute all tests, run:

```bash
./mvnw clean test