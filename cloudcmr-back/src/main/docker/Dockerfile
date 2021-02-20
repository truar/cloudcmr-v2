# Use AdoptOpenJDK for base image.
FROM adoptopenjdk:11-jdk-hotspot

RUN  apt-get update \
  && apt-get install -y wget \
  && rm -rf /var/lib/apt/lists/*

# Copy the jar to the production image from the builder stage.
COPY app.jar /app.jar

## Generate the appCDS

## Do not run by disabling the spring.devtools.restart.enabled as it will result in a failed startup because the
## application is not executed in Compute engine resource, which causes exception when starting datastore beans
RUN java -XX:DumpLoadedClassList=/classes.lst -jar /app.jar --kill
# From the captured list of classes (based on execution coverage),
# generate the AppCDS archive file.
RUN java -Xshare:dump -XX:SharedClassListFile=/classes.lst -XX:SharedArchiveFile=/appcds.jsa --class-path /app.jar

## Add Debugger Agent
RUN mkdir /opt/cdbg && \
     wget -qO- https://storage.googleapis.com/cloud-debugger/compute-java/debian-wheezy/cdbg_java_agent_gce.tar.gz \
     | tar xvz -C /opt/cdbg

## Add profiler agent
RUN mkdir -p /opt/cprof && \
    wget -q -O- https://storage.googleapis.com/cloud-profiler/java/latest/profiler_java_agent.tar.gz \
    | tar xzv -C /opt/cprof

# Disable DevTools as it is not disabled on cloud Run
# TODO : Find a way to exclude DevTools dependencies from the ShadowJar
# Run the web service on container startup.
CMD ["java", \
     "-Dspring.devtools.restart.enabled=false", \
    "-Xshare:on", \
     "-XX:SharedArchiveFile=/appcds.jsa", \
     "-jar", \
     "/app.jar"]
