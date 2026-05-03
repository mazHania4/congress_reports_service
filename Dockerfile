FROM eclipse-temurin:21-jre-jammy

WORKDIR /reports_service

COPY service.jar service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "reports_service.jar"]