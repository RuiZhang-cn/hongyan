FROM openjdk:8u201-jdk-alpine3.9
LABEL authors="rui zhang"
#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
VOLUME /tmp
ADD target/hongyan-0.0.1-SNAPSHOT.jar hongyan.jar
EXPOSE 80
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","hongyan.jar"]