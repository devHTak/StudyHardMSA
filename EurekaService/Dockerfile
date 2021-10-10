FROM openjdk:8-jdk-alpine
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} EurekaService.jar
ENTRYPOINT ["java", "-jar", "EurekaService.jar"]