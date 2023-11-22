FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml pom.xml
COPY src src
RUN  mvn clean compile
RUN  mvn package -DskipTests

FROM openjdk:17
COPY --from=build /app/target/project.war app.war
EXPOSE 9090
CMD ["java", "-jar", "app.war"]