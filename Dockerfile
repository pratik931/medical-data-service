# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS build
WORKDIR /medical-data-service
COPY . /medical-data-service
RUN ./gradlew clean build

# Stage 2: Create a lightweight container for runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /medical-data-service

# Copy the built JAR file from the previous stage
COPY --from=build /medical-data-service/build/libs/medical-data-service-0.0.1-SNAPSHOT.jar medical-data-service.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "medical-data-service.jar"]