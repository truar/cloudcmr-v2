package com.cloud.cmr.cloudcmrfunctions;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImportMembersService {

    private final GcpStorageResourceLoader loader;
    private final MemberGateway memberGateway;

    public ImportMembersService(GcpStorageResourceLoader loader, MemberGateway memberGateway) {
        this.loader = loader;
        this.memberGateway = memberGateway;
    }

    public void importMemberFromGcpStorage(String bucket, String name) {
        Resource resource = loader.loadResource(bucket, name);
        try(InputStream inputStream = resource.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr)) {

            List<MemberRest> members = reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] data = line.split(";");
                        return new MemberRest(data[3], data[2]);
                    }).collect(Collectors.toList());

            memberGateway.send(members);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the file [" + resource.getFilename() + "]", e);
        }
    }
}
