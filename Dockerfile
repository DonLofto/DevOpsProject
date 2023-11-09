FROM tomcat:10.1.15
RUN sed -i 's/port="8080"/port="9090"/' /usr/local/tomcat/conf/server.xml
#RUN rum rm -rf /usr/local/tomcat/webapps/*
ADD ./target/*.war /usr/local/tomcat/webapps/project.war
EXPOSE 9090
CMD ["catalina.sh", "run"]