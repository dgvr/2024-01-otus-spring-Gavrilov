FROM bellsoft/liberica-openjdk-alpine-musl:17.0.1
ARG JAR_FILE=/target/hw17-docker-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]