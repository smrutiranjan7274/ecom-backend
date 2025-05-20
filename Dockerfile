# Stage 1: Build JAR
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Run JAR
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN mkdir -p /app/data && chmod -R 777 /app/data
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]