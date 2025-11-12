# ============================
# 1️⃣ Build stage
# ============================
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Set working directory inside container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# ============================
# 2️⃣ Runtime stage
# ============================
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/target/smartExpenseTracker-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that Spring Boot runs on
EXPOSE 8080

# Set environment variables (Render will override with its own)
ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
