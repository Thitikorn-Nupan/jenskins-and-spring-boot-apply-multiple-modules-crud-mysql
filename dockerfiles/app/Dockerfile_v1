#
#
# **** Dockerfile to create spring boot image ****
#
#
### Use official base image of Java Runtime (JDK version that you use)
FROM openjdk:17
LABEL authors="ttknp"

### (Set folder on container) specific space to persist some data in your container.
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY /target/basic-api.jar /app

## Make port 8080 available to the world outside container
EXPOSE 8080

# Run java -jar /app/basic-api.jar when the container launches
CMD ["java","-jar","/app/basic-api.jar"]

# Step to run
# ** -t <tag name : version> and -f <path to Dockerfile_v.1>
# **  --build-arg <Key>=<Value>
# 1. docker build -t springboot:latest .  -f dockerfiles\app\Dockerfile
# ** -p 8080:80 maps port 8080 on the host machine to port 80 inside the container.
# ** -d run on background , --name=<container name> , -p <port remote to container>:<port app on container>
# ** these are run image and run mysql ? for sure! ** for the fist time
# 2. docker run --name springboot_app -p 8000:8080 -d springboot:latest
# way to access container
# 3. docker exec -it <container id> sh
# ** now you can use command as linux note this case mysql is on /var/lib/mysql/
# 3.1 exit ** for leaving container



