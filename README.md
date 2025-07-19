# Hacker News Articles API

A Spring Boot application that fetches and manages Hacker News articles about Java, 
with a RESTful API for querying and managing the articles.

## Features

- Hourly fetch of Java articles from Hacker News API
- Data persisted in PostgreSQL
- RESTful API with stateless JWT authentication
- Article filtering by author, tags, title, and month
- Pagination (5 items per page)
- Soft delete (non-destructive removal)
- Swagger/OpenAPI documentation
- Containerized with Docker & Docker Compose

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Gradle

## Getting Started

### 1. Clone the repository
```bash
git clone <repository-url>
cd test-apply
```

### 2. Environment Setup
Create a `.env` file in the root directory with the following variables:
```env
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_USER=your_username
DB_PASSWORD=your_password
DB_NAME=your_database_name

# JWT Configuration
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=1000000 # in milliseconds
```

### 3. Build and Run with Docker
```bash
docker-compose up --build
```

### 4. Access the Application
- API Base URL: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/docs`

## API Endpoints

### Authentication
- `POST /api/auth/login` - Get JWT token
  - Body: `{"username":"admin", "password":"password"}`
  - Add token to subsequent requests in the `Authorization` header: `Bearer <token>`

### Articles
- `GET /api/articles` - Get paginated articles
  - Query params: 
    - `page` (default: 0)
    - `author` (optional)
    - `tags` (comma-separated)
    - `title` (partial match)
    - `month` (e.g., "january")
- `DELETE /api/articles/{id}` - Soft delete an article

### Admin
- `POST /api/articles/refresh` - Force data refresh from Hacker News API

## Development

### Build and Run Locally
```bash
./gradlew bootRun
```

### Run Tests
```bash
./gradlew test
```

## Architecture

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Manages database operations
- **Scheduled Task**: Fetches new articles hourly
- **JWT Authentication**: Secures the API endpoints

## Assumptions & Choices

1. Used PostgreSQL for its stability and compatibility with JPA
2. Chose Spring Security with JWT to satisfy the authorization requirement (with hardcoded credentials for simplicity)
3. Adopted Spring Data Specification to support dynamic filtering by author, title, tags, and month
4. Modeled tags as an @ElementCollection to simplify persistence over full normalization
5. Skipped batch inserts to keep the challenge simple, though they are recommended for performance in production
6. Chose to use the field storyTitle as the main title reference, since some articles may also contain a title field(not used in this context) to avoid ambiguity and ensure consistent filtering