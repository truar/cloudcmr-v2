package com.cloud.cmr.cloudcmrfunctions;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class GcpStorageResourceLoader {

    private ApplicationContext context;

    public GcpStorageResourceLoader(ApplicationContext context) {
        this.context = context;
    }

    /**
     * The resource found in GCP bucket
     *
     * @param bucket the bucket name
     * @param file   the name file (must be relative to the bucket root)
     * @return the Resource found on GCP. If the resource does not exist, a runtimeException is thrown
     */
    public Resource loadResource(String bucket, String file) {
        String resourceLocation = String.format("gs://%s/%s", bucket, file);
        Resource resource = context.getResource(resourceLocation);
        if (!resource.exists()) {
            throw new RuntimeException("The file does not exist on the GCP Bucket [" + bucket + "]");
        }
        return resource;
    }
}
