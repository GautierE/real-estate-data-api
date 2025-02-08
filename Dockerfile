FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml ./

RUN mvn dependency:go-offline

COPY src/ ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar

COPY .env .env

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

