FROM maven:3.9.5-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 1337

ENTRYPOINT ["java", "-jar", "app.jar", "--SERVER_HOST=0.0.0.0", "--SERVER_PORT=1337"]