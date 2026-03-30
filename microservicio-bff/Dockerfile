FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/microservicio-bff-1.0.0.jar app.jar

EXPOSE 8080

ENV PORT=8080
ENV USUARIOS_SERVICE_URL=http://funcion-usuarios:8081
ENV PRESTAMOS_SERVICE_URL=http://funcion-prestamos:8082

ENTRYPOINT ["java", "-jar", "app.jar"]
