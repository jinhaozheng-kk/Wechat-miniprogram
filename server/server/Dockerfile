FROM eclipse-temurin:17-jre
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_ADDRESS=0.0.0.0
ENV SERVER_PORT=8080

COPY target/server-0.0.1-SNAPSHOT.jar /app/server.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/server.jar"]
