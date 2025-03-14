
FROM gradle:7.5.1-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
RUN gradle build -x test --no-daemon || return 0

COPY . .
RUN gradle clean build -x test --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]