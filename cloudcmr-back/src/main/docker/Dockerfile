# Use AdoptOpenJDK for base image.
FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine

# Copy the jar to the production image from the builder stage.
COPY app.jar /app.jar

## Generate the appCDS

## Do not run by disabling the spring.devtools.restart.enabled as it will result in a failed startup because the
## application is not executed in Compute engine resource, which causes exception when starting datastore beans
RUN java -XX:DumpLoadedClassList=/classes.lst -jar /app.jar --kill
# From the captured list of classes (based on execution coverage),
# generate the AppCDS archive file.
RUN java -Xshare:dump -XX:SharedClassListFile=/classes.lst -XX:SharedArchiveFile=/appcds.jsa --class-path /app.jar

# Disable DevTools as it is not disabled on cloud Run
# TODO : Find a way to exclude DevTools dependencies from the ShadowJar
# Run the web service on container startup.
CMD ["java", "-Xshare:on", "-XX:SharedArchiveFile=/appcds.jsa", "-Dspring.devtools.restart.enabled=false", "-jar", "/app.jar"]
