# Expense Tracker - Authentication Service

A Spring Boot microservice providing JWT-based authentication for the Expense Tracker application.

## Features

- JWT Authentication & Authorization
- User Registration & Login
- Role-based Access Control
- MySQL Database Integration
- Spring Security Configuration
- RESTful API Endpoints

## Tech Stack

- **Java 24**
- **Spring Boot 3.5.7**
- **Spring Security**
- **Spring Data JPA**
- **MySQL 8.0**
- **JWT (JSON Web Tokens)**
- **Gradle**

## Prerequisites

- Java 24 or higher
- MySQL 8.0+
- Gradle (included via wrapper)

## Database Setup

1. Install and start MySQL
2. Create database:
```sql
CREATE DATABASE authservice;
```

3. Create user and grant permissions:
```sql
CREATE USER 'root'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON authservice.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

## Environment Variables

Set the following environment variable:
```bash
export DB_PASSWORD=your_mysql_password
```

Or create a `.env` file:
```
DB_PASSWORD=your_mysql_password
```

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Set environment variables
4. Run the application:

```bash
./gradlew app:run
```

The application will start on `http://localhost:9898`

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | User login |
| POST | `/auth/refresh` | Refresh JWT token |

### Example Requests

**Register User:**
```bash
curl -X POST http://localhost:9898/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "testpass"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:9898/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser", 
    "password": "testpass"
  }'
```

## Configuration

Key configuration in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/authservice
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD:password}

# Server
server.port=9898

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
```

## Security

- Passwords are encrypted using BCrypt
- JWT tokens for stateless authentication
- Role-based authorization
- CORS configuration for cross-origin requests

## Development

### Build
```bash
./gradlew build
```

### Test
```bash
./gradlew test
```

### Clean
```bash
./gradlew clean
```

## Project Structure

```
AuthService/
├── app/
│   ├── src/main/java/org/example/
│   │   ├── auth/           # Security configuration
│   │   ├── controller/     # REST controllers
│   │   ├── Entities/       # JPA entities
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── App.java        # Main application
│   └── src/main/resources/
│       └── application.properties
├── gradle/
├── build.gradle
└── README.md
```

## Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License

This project is part of the Expense Tracker application suite.