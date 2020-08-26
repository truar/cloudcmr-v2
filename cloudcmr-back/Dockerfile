# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3-jdk-11-slim as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

# Build a release artifact.
COPY src ./src
RUN mvn package -DskipTests

# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/cloudcmr-*.jar /cloudcmr.jar

# Run the web service on container startup.
CMD ["java", "-jar", "/cloudcmr.jar"]