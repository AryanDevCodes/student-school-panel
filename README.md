# School Admin Portal

A comprehensive web-based School Administration Portal built with Java, Spring Boot, and Maven. This application enables school administrators to efficiently manage students, fees, and user accounts with a secure and user-friendly interface.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
  - [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Role-Based Access](#role-based-access)
- [Testing](#testing)
- [Contributing](#contributing)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## Features

- User authentication and role-based access control
- Student record CRUD operations
- Fee management and payment tracking
- User account management (create, update, delete)
- Responsive UI with Thymeleaf templates
- RESTful API endpoints for integration
- Configurable database (H2/MySQL)
- Error handling and validation

## Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- Thymeleaf
- Maven
- H2/MySQL (configurable)
- JUnit & Mockito (testing)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- (Optional) MySQL database

### Installation

1. **Clone the repository:**
    ```
    git clone https://github.com/AryanDevCodes/school-admin-portal.git
    cd school-admin-portal
    ```

2. **Build the project:**
    ```
    mvn clean install
    ```

### Running the Application

- **With H2 (default):**
    ```
    mvn spring-boot:run
    ```
    Access the portal at [http://localhost:8080](http://localhost:8080)

- **With MySQL:**
    - Update `src/main/resources/application.properties` with your MySQL credentials.
    - Ensure MySQL is running.
    - Run the application as above.

### Configuration

Edit `src/main/resources/application.properties` to configure:
- Database connection (H2/MySQL)
- Server port
- Security settings

Example for MySQL:
```
spring.datasource.url=jdbc:mysql://localhost:3306/school_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

## Project Structure

- `entity/` - JPA entities (e.g., UserEntity, StudentEntity)
- `repository/` - Spring Data repositories (e.g., UserRepository)
- `controller/` - REST and web controllers
- `service/` - Business logic and services
- `templates/` - Thymeleaf HTML templates
- `config/` - Security and application configuration
- `resources/` - Static resources and properties files

## Usage

- Register and log in as an admin, management, or student.
- Manage students: add, update, delete, and view student records (management/school roles).
- Manage fees: create, update, and track payments (management/school roles).
- Manage user accounts and roles (admin/management roles).
- Students can view their own details, marks, fees, and course information.

## Role-Based Access

The application uses Spring Security to enforce role-based access control:

- **STUDENT**: Can only view their own details, marks, fees, and course information. Cannot access management or admin features.
- **MANAGEMENT**: Can manage all student records, fees, and user accounts, but cannot access student-only dashboards.
- **SCHOOL**: (If applicable) Has similar or extended permissions as management.
- **ADMIN**: (If implemented) Has full access to all features and settings.

Access to each controller and view is restricted based on the user's assigned role. Attempting to access unauthorized pages will redirect to the login page or show an error.

## Testing

Run all tests with:
```
mvn test
```
Unit and integration tests are located in `src/test/java`.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

## Troubleshooting

- **Port already in use:** Change `server.port` in `application.properties`.
- **Database connection errors:** Verify your database credentials and URL.
- **Build issues:** Run `mvn clean install` to resolve dependency issues.

## License

This project is licensed under the MIT License.

---
