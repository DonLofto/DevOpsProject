FROM tomcat:latest

# Set the working directory in the container
WORKDIR /usr/local/tomcat/webapps

COPY pom.xml pom.xml

COPY src src

RUN mvn clean compile

RUN mvn package -DskipTests

EXPOSE 9090

CMD ["catalina.sh", "run"]