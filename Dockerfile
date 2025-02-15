FROM maven:3.8.4-openjdk-17-slim as build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources/2025_swift_codes.xlsx /app/src/main/resources/2025_swift_codes.xlsx

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]