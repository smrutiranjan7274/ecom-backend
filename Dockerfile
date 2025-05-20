# Stage 1: Build the JAR using Maven/Gradle
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app
# Copy ALL project files (including pom.xml, src, etc.)
COPY . .  

# For Maven projects:
RUN ./mvnw clean package -DskipTests

# For Gradle projects (use this instead of Maven):
# RUN ./gradlew bootJar -DskipTests

# --------------------------------------------------------

# Stage 2: Run the JAR
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the JAR from the builder stage
# For Maven:
COPY --from=builder /app/target/*.jar app.jar

# For Gradle (replace with this):
# COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]