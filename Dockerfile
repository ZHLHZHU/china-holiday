# Part 1: Build the app using Maven
FROM maven:3.8.5-jdk-11 AS build

## download dependencies
ADD pom.xml /
RUN mvn dependency:go-offline

## build after dependencies are down so it wont redownload unless the POM changes
ADD . /
RUN mvn package

# Part 2: use the JAR file used in the first part and copy it across ready to RUN
FROM openjdk:8-jdk-alpine
WORKDIR /root/
## COPY packaged JAR file and rename as app.jar
## → this relies on your MAVEN package command building a jar
## that matches *-jar-with-dependencies.jar with a single match
COPY --from=build /target/*-jar-with-dependencies.jar app.jar
EXPOSE 8060
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./app.jar"]