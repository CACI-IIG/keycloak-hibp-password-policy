FROM maven:3-jdk-8-alpine AS build
WORKDIR /opt/build

COPY pom.xml pom.xml
RUN mvn install

COPY src src
RUN mvn clean package

FROM jboss/keycloak:4.4.0.Final

COPY --from=build /opt/build/target/keycloak-hibp-policy*.jar keycloak/standalone/deployments/hibp-password-policy.jar
