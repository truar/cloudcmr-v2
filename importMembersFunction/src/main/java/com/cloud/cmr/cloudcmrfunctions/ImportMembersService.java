package com.cloud.cmr.cloudcmrfunctions;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Service
public class ImportMembersService {

    private final GcpStorageResourceLoader loader;

    public ImportMembersService(GcpStorageResourceLoader loader) {
        this.loader = loader;
    }

    public void importMemberFromGcpStorage(String bucket, String name) {
        Resource resource = loader.loadResource(bucket, name);
        try(InputStream inputStream = resource.getInputStream()) {
            String s = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
            System.out.println(s);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the file [" + resource.getFilename() + "]", e);
        }
    }
}
