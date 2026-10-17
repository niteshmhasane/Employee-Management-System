# Employee Management System

A full-stack Employee Management System built with Java, Spring Boot, Spring Data JPA, Hibernate and MySQL. It has a REST API for managing employee records, a simple HTML, CSS and JavaScript frontend served by Spring Boot, input validation, global exception handling, a startup data seeder and automated tests.

## Tech Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3 (Spring Web, Spring Data JPA)
- **ORM:** Hibernate
- **Database:** MySQL
- **Validation:** Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Email`, `@Positive`)
- **Frontend:** HTML, CSS, JavaScript (served from Spring Boot's `static` folder)
- **Testing:** JUnit 5, MockMvc (Spring Boot Test)
- **Build tool:** Maven

## Features
- Create, view, update and delete employees through REST endpoints.
- Layered architecture: Controller, Service, Repository and Entity.
- Input validation with clear error messages for invalid requests.
- Global exception handling with `@RestControllerAdvice`: validation errors return 400 and missing employees return 404.
- Startup data seeder (`DataInitializer`, a `CommandLineRunner`) that fills the database up to 1,000 sample employees when it has fewer.
- Web page to add, edit, search and delete employees.

## Project Structure
```text
src/main/java/com/Nitzz/employeemanagementsystem/
├── config/         DataInitializer (seeds 1,000 sample employees)
├── controller/     EmployeeController (REST endpoints)
├── entity/         Employee
├── exception/      GlobalExceptionHandler, ResourceNotFoundException
├── repository/     EmployeeRepository (Spring Data JPA)
└── service/        EmployeeService and EmployeeServiceImpl
src/main/resources/
├── application.properties
└── static/         index.html, style.css, script.js
src/test/java/...   EmployeeControllerTest (MockMvc tests)
```

## Getting Started

### Prerequisites
- Java 17 or later
- MySQL Server
- Maven (or use the included Maven wrapper)

### 1. Clone the repository
```bash
git clone https://github.com/niteshmhasane/Employee-Management-System.git
cd Employee-Management-System
```

### 2. Create the database
```sql
CREATE DATABASE employee_db;
```

### 3. Configure MySQL
Open `src/main/resources/application.properties` and set your own MySQL username and password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```
Do not commit your real password.

### 4. Run the application
```bash
./mvnw spring-boot:run
```
On the first start, the seeder adds sample employees until the table has 1,000 records.

### 5. Open the app
Go to http://localhost:8080/ for the web page. The API is at http://localhost:8080/api/employees.

## REST API

| Method | Endpoint | Description | Success | Errors |
|--------|----------|-------------|---------|--------|
| GET | `/api/employees` | Get all employees | 200 | none |
| GET | `/api/employees/{id}` | Get one employee | 200 | 404 if not found |
| POST | `/api/employees` | Create an employee | 200 | 400 if invalid |
| PUT | `/api/employees/{id}` | Update an employee | 200 | 400 if invalid, 404 if not found |
| DELETE | `/api/employees/{id}` | Delete an employee | 200 | 404 if not found |

**Example request body**
```json
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "department": "IT",
  "salary": 45000
}
```

**Validation rules**
- `name`, `email` and `department` must not be blank.
- `email` must be a valid email address and must be unique.
- `salary` is required and must be greater than zero.

**Example error responses**

400 Bad Request (invalid data):
```json
{ "name": "Name is required", "email": "Invalid email format" }
```

404 Not Found (missing employee):
```json
{ "error": "Employee not found with id: 99" }
```

## Testing
The project has 10 MockMvc controller tests in `EmployeeControllerTest`. They cover:
- listing and fetching employees
- creating and updating employees with valid data
- 400 responses for a blank name, an invalid email and a negative salary
- 404 responses when an employee does not exist (get and delete)
- deleting an employee

Run the tests with:
```bash
./mvnw clean test
```
The default `contextLoads` test starts the whole application, so MySQL must be running.

## Author
**Nitesh Mhasane**
- GitHub: [niteshmhasane](https://github.com/niteshmhasane)
- LinkedIn: [nitesh-mhasane](https://www.linkedin.com/in/nitesh-mhasane-527005252)