FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY target/kata-bank-0.0.1-SNAPSHOT.jar kata-bank-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/kata-bank-0.0.1-SNAPSHOT.jar"]