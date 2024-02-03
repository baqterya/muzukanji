FROM openjdk:latest
LABEL authors="baqterya"
MAINTAINER baqterya
COPY target/muzukanji-1.0-RELEASE.jar muzukanji-1.0.jar
ENV POSTGRES_USER=${KEYCLOAK_USER}
ENV POSTGRES_PASS=${POSTGRES_PASS}
ENTRYPOINT ["java","-jar","/muzukanji-1.0.jar"]