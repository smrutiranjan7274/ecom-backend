# Stage 1: Build the JAR
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app
# Copy all files (including mvnw and .mvn/wrapper)
COPY . .  

# Fix permissions for Maven Wrapper
RUN chmod +x mvnw  # Grant execute permission

# Build the JAR
RUN ./mvnw clean package -DskipTests  # Now this will work

# ------------------------------------

# Stage 2: Run the JAR
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]