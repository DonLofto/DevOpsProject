FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml pom.xml
COPY src src
RUN mvn clean compile
RUN mvn package -DskipTests


FROM tomcat:latest

COPY --from=build /app/target/app.war  /usr/local/tomcat/webapps/app.war

EXPOSE 9090

CMD ["catalina.sh", "run"]