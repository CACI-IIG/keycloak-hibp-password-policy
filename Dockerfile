FROM maven:3-jdk-8-alpine AS build
WORKDIR /opt/build

COPY pom.xml pom.xml
RUN mvn install

COPY src src
RUN mvn clean package

FROM jboss/keycloak:4.4.0.Final
#ENV DB_VENDOR=POSTGRES

#COPY configuration/configuration.cli /opt/jboss/keycloak/configuration.cli
#COPY configuration/add-event-listener.cli /opt/add-event-listener.cli
#RUN cd /opt/jboss/keycloak ; bin/jboss-cli.sh --file=configuration.cli ; rm -rf /opt/jboss/keycloak/standalone/configuration/standalone_xml_history

COPY --from=build /opt/build/target/keycloak-hibp-policy*.jar keycloak/standalone/deployments/hibp-password-policy.jar
