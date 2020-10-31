package com.cloud.cmr.cloudcmrfunctions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ImportMembersFunctionTests {

    private static final String BUCKET_NAME = "bucket_test";
    private static final String FILE_NAME = "file_name.csv";

    @Autowired
    private ImportMembersFunction function;

    @MockBean
    private GcpStorageResourceLoader gcpStorageResourceLoader;

    @MockBean
    private PubSubTemplate pubSubTemplate;

    @Test
    void batch_members_request_is_sent_when_receiving_uploaded_file_event() {
        GscEvent gscEvent = new GscEvent();
        gscEvent.setBucket(BUCKET_NAME);
        gscEvent.setName(FILE_NAME);
        when(gcpStorageResourceLoader.loadResource(BUCKET_NAME, FILE_NAME))
                .thenReturn(getTestResource());

        function.gcsEvent().accept(gscEvent);

        verify(gcpStorageResourceLoader).loadResource(BUCKET_NAME, FILE_NAME);
        verify(pubSubTemplate).publish(eq("importMemberTopic"), any(MemberDTO.class));
    }

    private Resource getTestResource() {
        String s = "EMPTY_HEADER_LINE\n" +
                "PC11737989;;doe;john;M;1967;01/01/1967;1 chemin de la localisation;;Lieu-Dit;12345;Ma Ville;FRANCE;0102030405;0601020304;john.doe@mail.fr;NON;RCA;2018;26/01/2018;19:28:13;26/01/2018;14/10/2018;;PC";
        return new ByteArrayResource(s.getBytes());
    }
}
