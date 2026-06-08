# Smart Expense Tracker

A production-ready Spring Boot 3 personal finance backend application built for hackathon.

## Features

- User registration and account management
- Expense tracking with categorization
- Budget management and monitoring
- Advanced analytics and spending trends
- AI-generated spending insights
- JWT-based authentication
- Comprehensive API documentation with Swagger

## Technology Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security with JWT
- PostgreSQL
- Lombok
- MapStruct
- Flyway Migrations
- OpenAPI/Swagger
- Maven

## Quick Start

### Prerequisites

- Java 21+
- PostgreSQL 14+
- Maven 3.8+

### Installation

1. Clone the repository
```bash
git clone https://github.com/ivonnedpa/smart-expense-tracker-hackathon.git
cd smart-expense-tracker-hackathon
```

2. Configure PostgreSQL in `application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/expense_tracker
    username: postgres
    password: your_password
```

3. Build and run
```bash
mvn clean install
mvn spring-boot:run
```

### Access Swagger UI

Once the application is running, navigate to:
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Expenses
- `POST /api/expenses` - Create expense
- `GET /api/expenses` - Get all expenses (paginated)
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense

### Budgets
- `POST /api/budgets` - Create budget
- `GET /api/budgets` - Get all budgets
- `GET /api/budgets/status` - Get budget status

### Analytics
- `GET /api/analytics/monthly-summary` - Monthly spending summary
- `GET /api/analytics/category-breakdown` - Breakdown by category
- `GET /api/analytics/trends` - Last 6 months trends

### AI Insights
- `GET /api/ai/insights` - Get AI-generated spending insights

### Dashboard
- `GET /api/dashboard` - Get dashboard overview

## Project Structure

```
src/main/java/com/hackathon/expensetracker
├── config/              # Spring configuration
├── controller/          # REST controllers
├── service/             # Business logic
├── repository/          # Data access layer
├── entity/              # JPA entities
├── dto/                 # Data transfer objects
├── mapper/              # MapStruct mappers
├── security/            # JWT & security
├── exception/           # Custom exceptions
├── analytics/           # Analytics service
├── ai/                  # AI insights
└── util/                # Utilities
```

## Database Schema

- `users` - User accounts
- `categories` - Expense categories
- `expenses` - Expense records
- `budgets` - Budget allocations

## Sample Data

The application includes a CommandLineRunner that automatically loads:
- 1 demo user (john@example.com / Password123)
- 8 predefined categories
- 50 realistic expenses
- 5 active budgets

## Security

- JWT authentication for all protected endpoints
- Password encryption with BCrypt
- Role-based access control
- Protected endpoints require valid Bearer token

## Testing

Run tests with:
```bash
mvn test
```

## License

MIT
