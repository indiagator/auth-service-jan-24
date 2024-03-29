
#INSTALLATION OF THE OPERATING SYSTEM
FROM adoptopenjdk/maven-openjdk11

LABEL authors="indiagator"

#PLACEMENT OF THE EXECUTABLE [MICROSERVICE] ON THE IMAGE
COPY target/auth-service-jan-24-prod-1.jar app.jar

#EXPOSE PORTS FOR INCOMING TRAFFIC - HOST_PORT:CONTAINER_PORT
EXPOSE 8088:8088

#INSTALLING UTILITIES
RUN apt-get update
RUN apt-get install -y gcc
RUN apt-get install -y curl

#ENTRYPOINT OF THE CONTAINER THROUGH THE MICROSERVICE
ENTRYPOINT ["java","-jar","app.jar"]