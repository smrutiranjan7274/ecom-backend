# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Delay for Railway networking to initialize (optional)
ENTRYPOINT ["sh", "-c", "sleep 10 && java -Xmx256m -Xms128m -jar app.jar --server.port=${PORT:-8080} --server.address=0.0.0.0"]
