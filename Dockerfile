FROM tomcat:latest

# Set the working directory in the container
WORKDIR /usr/local/tomcat/webapps

# Copy the Spring Boot WAR file into the container
COPY target/app.war /usr/local/tomcat/webapps/

EXPOSE 9090

CMD ["catalina.sh", "run"]