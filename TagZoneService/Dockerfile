FROM openjdk:8-jdk-alpine
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} TagZoneService.jar
ENTRYPOINT ["java", "-jar", "./target/TagZoneService.jar"]