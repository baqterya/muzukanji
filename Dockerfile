FROM openjdk:latest
LABEL authors="baqterya"
MAINTAINER baqterya
COPY target/muzukanji-1.0-RELEASE.jar muzukanji-1.0-RELEASE.jar
ENTRYPOINT ["java","-jar","/muzukanji-1.0-RELEASE.jar"]