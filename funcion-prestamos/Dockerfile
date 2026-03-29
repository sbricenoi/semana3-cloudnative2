FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/funcion-prestamos-1.0.0.jar app.jar
COPY ../wallet /app/wallet

EXPOSE 8082

ENV PORT=8082
ENV DB_URL=jdbc:oracle:thin:@(description=(retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.sa-santiago-1.oraclecloud.com))(connect_data=(service_name=g64afca1579a0d2_s58onuxcx4c1qxe9_high.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))
ENV DB_USER=biblioteca
ENV DB_PASSWORD=#Ng3naUQa*THhTd
ENV ORACLE_NET_TNS_ADMIN=/app/wallet

ENTRYPOINT ["java", "-jar", "app.jar"]
