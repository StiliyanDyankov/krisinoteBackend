FROM openjdk:17
EXPOSE 8080
ARG JAR_FILE=target/krisinoteBackend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} .
CMD [ "java", "-jar",  "/krisinoteBackend-0.0.1-SNAPSHOT.jar"]