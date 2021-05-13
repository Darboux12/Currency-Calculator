FROM openjdk:11
MAINTAINER dariusz.bula
EXPOSE 8080
WORKDIR /calc
COPY target/calculator-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "calculator-0.0.1-SNAPSHOT.jar"]

