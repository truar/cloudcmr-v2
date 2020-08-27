# Use the gradlew to build image. Should be cached with kaniko
FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine as builder

# Copy local code to the container image.
WORKDIR /app
COPY ../../../build.gradle .
COPY ../../../gradlew .
COPY ../../../gradle ./gradle
RUN ./gradlew dependencies

# Build a release artifact.
COPY ../.. ./src
RUN ./gradlew build -x test

# Use AdoptOpenJDK for base image.
FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/build/libs/*.jar /cloudcmr.jar

# Run the web service on container startup.
CMD ["java", "-jar", "/cloudcmr.jar"]