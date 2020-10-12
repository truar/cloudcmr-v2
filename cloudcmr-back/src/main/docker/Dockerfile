# Use the gradlew to build image. Should be cached with kaniko
FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine as builder

# Copy local code to the container image.
WORKDIR /app
COPY build.gradle .
COPY gradlew .
COPY gradle ./gradle
RUN ./gradlew dependencies

# Build a release artifact.
COPY src ./src
RUN ./gradlew build shadowJar -x test -x bootJar
RUN cp /app/build/libs/*-all.jar /app.jar

# Use AdoptOpenJDK for base image.
FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app.jar /app.jar

## Generate the appCDS
RUN java -XX:DumpLoadedClassList=/classes.lst -jar /app.jar --kill
# From the captured list of classes (based on execution coverage),
# generate the AppCDS archive file.
RUN java -Xshare:dump -XX:SharedClassListFile=/classes.lst -XX:SharedArchiveFile=/appcds.jsa --class-path /app.jar

# TODO : Try to find a way to make this work with Cloud Run automatically (using env configuration in YAML service file)
#ENV JAVA_TOOL_OPTIONS="-noverify -XX:+TieredCompilation -XX:TieredStopAtLevel=1"


# Disable DevTools as it is not disabled on cloud Run
# TODO : Find a way to exclude DevTools dependencies from the ShadowJAr
# Run the web service on container startup.
CMD ["java", "-XX:+TieredCompilation", "-XX:TieredStopAtLevel=1", "-Xshare:on", "-XX:SharedArchiveFile=/appcds.jsa", "-Dspring.devtools.restart.enabled=false", "-jar", "/app.jar"]
