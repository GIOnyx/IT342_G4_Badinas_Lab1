FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy built jar if present; for dev we use mvn in host. Keep multi-stage optional.
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3060
ENTRYPOINT ["sh","-c","java -jar /app/app.jar"]
