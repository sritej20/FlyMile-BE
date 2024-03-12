FROM openjdk:17

ADD target/flymile.jar /flymile.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "flymile.jar"]
