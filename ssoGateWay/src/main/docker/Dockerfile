FROM openjdk:8-jdk-alpine
ADD target/ssoGateWay-0.0.1-SNAPSHOT.jar ssoGateway.jar
EXPOSE 10202
ENTRYPOINT ["java", "-jar", "ssoGateway.jar"]