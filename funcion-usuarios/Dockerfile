FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/funcion-usuarios-1.0.0.jar app.jar

EXPOSE 8081

ENV PORT=8081
ENV DB_URL=jdbc:oracle:thin:@<HOST>:<PUERTO>/<SERVICE_NAME>
ENV DB_USER=biblioteca
ENV DB_PASSWORD=#Ng3naUQa*THhTd

ENTRYPOINT ["java", "-jar", "app.jar"]
