FROM openjdk:17-jdk-alpine

COPY target/SBPostgDocRest-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "/app/SBPostgDocRest-0.0.1-SNAPSHOT.jar"]