FROM openjdk:17-jdk-slim
WORKDIR /app
COPY /target/system-1.0.1.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]