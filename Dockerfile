# Build stage
FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
RUN chmod +x gradlew

COPY . .

RUN ./gradlew spotlessApply && ./gradlew clean build -x test

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]