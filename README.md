# Hacker News Articles API

A Spring Boot application that fetches and manages Hacker News articles about Java, with a RESTful API for querying and managing the articles.

## Features

- Fetches Java-related articles from Hacker News API hourly
- Stores articles in a PostgreSQL database
- RESTful API with JWT authentication
- Filterable by author, tags, title, and month
- Paginated results (5 items per page)
- Soft delete functionality
- Swagger API documentation
- Docker support

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
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/hn_articles
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION_MS=86400000
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
- `POST /api/admin/refresh` - Force data refresh from Hacker News API

## Development

### Build and Run Locally
```bash
./gradlew bootRun
```

### Run Tests
```bash
./gradlew test
```

### Check Test Coverage
```bash
./gradlew jacocoTestReport
```

## Architecture

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Manages database operations
- **Scheduled Task**: Fetches new articles hourly
- **JWT Authentication**: Secures the API endpoints

## Database Schema

- `article`: Stores article data from Hacker News
- `deleted_articles`: Tracks soft-deleted articles to prevent re-insertion

## Assumptions & Choices

1. Used PostgreSQL for its robustness and full-text search capabilities
2. Implemented soft delete to track deleted articles
3. Added monthly filtering for better search functionality
4. Used JWT for stateless authentication
5. Scheduled task runs hourly to fetch new articles
6. Optimized batch inserts for better performance
