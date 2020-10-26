package com.cloud.cmr.cloudcmrfunctions;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

class ImportMembersServiceTest {

    private static final String BUCKET_NAME = "bucket_test";
    private static final String FILE_NAME = "file_name.csv";

    @Test
    void read_members_from_loader_and_send_a_request_to_a_gateway() {
        GcpStorageResourceLoader loader = mock(GcpStorageResourceLoader.class);
        when(loader.loadResource(BUCKET_NAME, FILE_NAME))
                .thenReturn(getTestResource());
        MemberGateway memberGateway = mock(MemberGateway.class);
        ImportMembersService service = new ImportMembersService(loader, memberGateway);

        service.importMemberFromGcpStorage(BUCKET_NAME, FILE_NAME);

        verify(memberGateway).send(asList(
           new MemberRest("john", "doe"),
           new MemberRest("alice", "bob")
        ));
    }

    private Resource getTestResource() {
        String s = "EMPTY_HEADER_LINE\n" +
                "PC11737989;;doe;john;M;1967;01/01/1967;1 chemin de la localisation;;Lieu-Dit;12345;Ma Ville;FRANCE;;;john.doe@mail.fr;NON;RCA;2018;26/01/2018;19:28:13;26/01/2018;14/10/2018;;PC\n" +
                "PC11737989;;bob;alice;M;1967;01/01/1967;1 chemin de la localisation;;Lieu-Dit;12345;Ma Ville;FRANCE;;;john.doe@mail.fr;NON;RCA;2018;26/01/2018;19:28:13;26/01/2018;14/10/2018;;PC\n";
        return new ByteArrayResource(s.getBytes());
    }
}