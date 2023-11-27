FROM tomcat:latest

WORKDIR /usr/local/tomcat/webapps/

COPY /target/*.war  app.war

EXPOSE 9090

CMD ["catalina.sh", "run"]