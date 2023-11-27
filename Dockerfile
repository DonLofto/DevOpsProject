FROM tomcat:latest

WORKDIR /usr/local/tomcat/webapps/

ADD /target/*.war  /usr/local/tomcat/webapps/*.war

EXPOSE 9090

CMD ["catalina.sh", "run"]