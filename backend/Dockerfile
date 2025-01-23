from alpine:latest

RUN apk add openjdk21-jdk

RUN mkdir /rooster

COPY rooster-0.0.1-SNAPSHOT.jar /rooster/rooster.jar

WORKDIR /rooster

ENTRYPOINT ["java", "-jar", "rooster.jar"]