FROM openjdk:latest
LABEL authors="baqterya"
MAINTAINER baqterya
COPY target/muzukanji-1.0-RELEASE.jar muzukanji-1.0.jar
ARG postgres_user
ARG postgres_pass
ENV POSTGRES_USER=$postgres_user
ENV POSTGRES_PASS=$postgres_pass
ENTRYPOINT ["java","-jar","/muzukanji-1.0.jar"]