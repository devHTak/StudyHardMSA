FROM openjdk:8-jdk-alpine
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} StudyService.jar
ENTRYPOINT ["java", "-jar", "./target/StudyService.jar"]