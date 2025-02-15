# Build stage
FROM maven:3.8.4-openjdk-17-slim as build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Final stage
FROM openjdk:17-slim

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Copy the Excel file to the resources folder in the container
COPY src/main/resources/2025_swift_codes.xlsx /app/src/main/resources/2025_swift_codes.xlsx

EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]