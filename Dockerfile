FROM openjdk:17-jre-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prd", "-jar", "app.jar"]
