### Use official base image of Java Runtime (JDK version that you use)
FROM openjdk:24-ea-17-jdk-slim

LABEL authors="ttknp"

WORKDIR /app

ARG JAR_FILE

ADD $JAR_FILE /app/application.jar

## Make port 8080 available to the world outside container
EXPOSE 8080

# for running spring boot in container
# Like you run application on your current path
# java -jar target/basic-api-in-docker-container.jar
ENTRYPOINT ["java","-jar","/app/application.jar"]


# When you run java to some plan ** Use only jar file will be cool !
# Step to run
# ** -t <tag name : version> and -f <path to Dockerfile_v.1>
# **  --build-arg <Key>=<Value>
# 1. docker build -t basic-api:latest --build-arg JAR_FILE=target/basic-api-using-spring-boot-3-0.0.1-SNAPSHOT.jar . -f dockerfiles\app\Dockerfile
# ** -p 8080:80 maps port 8080 on the host machine to port 80 inside the container.
# ** -d run on background , --name=<container name> , -p <port remote to container>:<port app on container>
# ** these are run image and run mysql ? for sure! ** for the fist time
# 2. docker run --name backend -p 8080:8080 -d basic-api:latest
# way to access container
# 3. docker exec -it <container id> sh
# ** you can do same cmd as you work on linux
# ** java -version , ls -l , mkdir <folder name> , ...
# 3.1 exit ** for leaving container


