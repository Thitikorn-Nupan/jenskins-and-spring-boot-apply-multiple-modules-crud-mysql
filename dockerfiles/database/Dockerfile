#
#
# **** Dockerfile to create mysql image **** **** (Dynmic env) Passing Environment Variables Into a Dockerfile *****
#
#
# Use the official MySQL image as a base # FROM have to the first line
FROM mysql:latest
LABEL authors="ttknp"

RUN chown -R mysql:root /var/lib/mysql/

# We can access ENV values during the build, as well as once the container runs
# Dockerfile_v.1 doesn’t provide a dynamic tool to set an ENV value during the build process.
# We have to use ARG To Hold --build-arg when we pass on build image Dockerfile_v.1
# Sign = doesn't need
ARG USERNAME
ARG PASSWORD
ARG DATABASE


# Set environment variables for the new user
# MYSQL_USER and MYSQL_PASSWORD mysql จะกำหนดสิทธิของ user ให้มีสิทธิทุกอย่างใน database(GRANT ALL) ที่เราได้กำหนดไว้ในตัวแปรนี้ and create database
ENV MYSQL_USER $USERNAME
ENV MYSQL_PASSWORD $PASSWORD
ENV MYSQL_DATABASE $DATABASE

# Expose port 3306 to allow connections to the database
EXPOSE 3306

# Step to run
# ** -t <tag name : version> and -f <path to Dockerfile_v.1>
# **  --build-arg <Key>=<Value>
# 1. docker build -t mysql:latest --build-arg USERNAME=ttknp --build-arg PASSWORD=12345 --build-arg DATABASE=to_do_apps_docker .  -f dockerfiles\database\Dockerfile
# ** -p 8080:80 maps port 8080 on the host machine to port 80 inside the container.
# ** -d run on background , --name=<container name> , -p <port remote to container>:<port app on container>
# ** these are run image and run mysql ? for sure! ** for the fist time
# 2.0 docker run -d --name=mysql_database -p 3307:3306 -e MYSQL_ROOT_PASSWORD=12345 -it mysql:latest
# 2.1 docker run --name mysql_database -p 3307:3306 -e MYSQL_ROOT_PASSWORD=12345 -d mysql:latest
# way to access container
# 3. docker exec -it <container id> sh
# ** now you can use command as linux note this case mysql is on /var/lib/mysql/
# 3.1 exit ** for leaving container

