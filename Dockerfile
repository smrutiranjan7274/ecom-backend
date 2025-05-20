# Stage 1: Build JAR
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Run JAR
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Create writable directory for H2
RUN mkdir -p /app/data && chmod -R 777 /app/data
COPY --from=builder /app/target/*.jar app.jar
# Add startup delay + dynamic port
ENTRYPOINT ["sh", "-c", "sleep 10 && java -Xmx256m -Xms128m -jar app.jar --server.port=${PORT} --server.address=0.0.0.0"]