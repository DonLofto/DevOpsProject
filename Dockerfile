# This Dockerfile is used to build and run the Java application.

# The 'FROM' instruction initializes a new build stage and sets the base image for subsequent instructions.
# Here, we are using the maven:3.8.5-openjdk-17 image as our base image for the build stage.
FROM maven:3.8.5-openjdk-17 AS build

# The 'WORKDIR' instruction sets the working directory for any instructions that follow it in the Dockerfile.
# Here, we are setting /app as our working directory.
WORKDIR /app

# The 'COPY' instruction copies new files from source and adds them to the filesystem of the container at the path.
# Here, we are copying the pom.xml and src directory from our source to the working directory in the container.
COPY pom.xml pom.xml
COPY src src

# The 'RUN' instruction will execute any commands in a new layer on top of the current image and commit the results.
# Here, we are running 'mvn clean compile' to compile our Java application.
RUN mvn clean compile

# Here, we are running 'mvn package -DskipTests' to package our application and skip the tests.
RUN mvn package -DskipTests

# The 'FROM' instruction initializes a new build stage and sets the base image for subsequent instructions.
# Here, we are using the openjdk:17 image as our base image for the run stage.
FROM openjdk:17

# The 'COPY' instruction copies new files from source and adds them to the filesystem of the container at the path.
# Here, we are copying the packaged .war file from the build stage to the run stage.
COPY --from=build /app/target/project.war app.war

# The 'EXPOSE' instruction informs Docker that the container listens on the specified network ports at runtime.
# Here, we are exposing port 8081.
EXPOSE 8081

# The 'CMD' instruction provides defaults for an executing container.
# Here, we are setting the default command to run our application when the container starts.
CMD ["java", "-jar", "app.war"]
