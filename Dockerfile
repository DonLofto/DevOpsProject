FROM tomcat:latest

WORKDIR /usr/local/tomcat/webapps/

ADD ./target/*.war  /usr/local/tomcat/webapps/app.war

EXPOSE 9090

CMD ["catalina.sh", "run"]