FROM openjdk:11-jre-slim

COPY target/classified-lifecycle-poc-eo-1.0.0.jar /app/app.jar

COPY Badwords.txt /app/Badwords.txt

WORKDIR /app

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
