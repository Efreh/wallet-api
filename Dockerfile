FROM openjdk:17-jdk-slim
LABEL authors="Efreh"
VOLUME /tmp
COPY target/wallet-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
