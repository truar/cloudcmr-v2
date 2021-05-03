# Use AdoptOpenJDK for base image.
FROM adoptopenjdk:11-jdk-hotspot

RUN  apt-get update \
  && apt-get install -y wget \
  && rm -rf /var/lib/apt/lists/*

# Copy the jar to the production image from the builder stage.
COPY app.jar /app.jar

## Add Debugger Agent
RUN mkdir /opt/cdbg && \
     wget -qO- https://storage.googleapis.com/cloud-debugger/compute-java/debian-wheezy/cdbg_java_agent_gce.tar.gz \
     | tar xvz -C /opt/cdbg

## Add profiler agent
RUN mkdir -p /opt/cprof && \
    wget -q -O- https://storage.googleapis.com/cloud-profiler/java/latest/profiler_java_agent.tar.gz \
    | tar xzv -C /opt/cprof

# Run the web service on container startup.
CMD ["java", \
    "-Dcom.google.cdbg.breakpoints.enable_canary=true", \
     "-jar", \
     "/app.jar"]
